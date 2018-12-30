(ns gratitude.data-generators
  (:require [clojure.pprint :as pprint]
            [clojure.string :as string]
            [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as sgen]
            [gratitude.user :as user]
            [gratitude.expression :as expression]
            [clojure.test.check.generators]))

;; gratitude.user generators

(s/def ::sane-avatar-url
  (-> #(re-matches user/avatar-url-regex %)
      (s/with-gen #(sgen/fmap string/join
                      (sgen/tuple
                        (s/gen #{"http://" "https://"})
                        (sgen/such-that seq (sgen/string-alphanumeric))
                        (s/gen #{".com" ".us" ".co" ".org"})
                        (sgen/return "/")
                        (sgen/string-alphanumeric))))))

(s/def ::insane-avatar-url
  (-> #(re-matches user/avatar-url-regex %)
      (s/with-gen #(sgen/fmap string/join
                      (sgen/tuple
                        (s/gen #{"http://" "https://"})
                        (sgen/string-alphanumeric)
                        (s/gen #{".com" ".us" ".co" ".org"})
                        (s/gen string?))))))

(s/def ::sane-email-address
  (-> string?
      (s/with-gen #(sgen/fmap string/join
                      (sgen/tuple
                        (sgen/such-that seq (sgen/string-alphanumeric))
                        (sgen/return "@")
                        (sgen/such-that seq (sgen/string-alphanumeric))
                        (s/gen #{".com" ".us" ".co" ".org"}))))))

(s/def ::insane-email-address
  (-> string?
      (s/with-gen #(sgen/fmap string/join
                      (sgen/tuple
                        (s/gen string?)
                        (s/gen #{"@" " "})
                        (s/gen string?)
                        (sgen/return ".")
                        (s/gen string?))))))


(s/def ::user-id
  (-> string?
      (s/with-gen #(sgen/frequency
                      [[9 (s/gen ::sane-email-address)]]
                      [[1 (s/gen ::insane-email-address)]]))))

(defn insane-string-generator
  [gen-fn]
  #(sgen/frequency
      [[9 (gen-fn)]]
      [[1 (sgen/string-alphanumeric)]]
      [[1 (s/gen string?)]]))

(def sane-first-name
  #(s/gen (into #{} (string/split "Su,Jo,Bob,Al,Bil,Jon," ","))))

(def insane-first-name
  (insane-string-generator sane-first-name))

(def sane-last-name
  #(s/gen (into #{} (string/split "Smith,Rogers,Jones,X,Ling,Shih" ","))))

(def insane-last-name
  (insane-string-generator sane-last-name))


(def sane-full-name
  #(sgen/frequency
      [[6 (sgen/fmap (partial string/join " ") ;   "first last"
              (sgen/tuple
                (sane-first-name)
                (sane-last-name)))]
       [2 (sgen/fmap (partial string/join ", ")  ; "last, first"
              (sgen/tuple
                (sane-last-name)
                (sane-first-name)))]
       [2 (sgen/fmap (partial string/join " ")   ; "name name name"
                (sgen/list (sane-first-name)))]]))

(def insane-full-name
  #(sgen/frequency
      [[6 (sgen/fmap (partial string/join " ") ;   "first last"
              (sgen/tuple
                (insane-first-name)
                (insane-last-name)))]
       [2 (sgen/fmap (partial string/join ", ")  ; "last, first"
              (sgen/tuple
                (insane-last-name)
                (insane-first-name)))]
       [2 (sgen/fmap (partial string/join " ")   ; "name name name"
                (sgen/list (insane-first-name)))]]))

(defn with-sane-user-generators
  [generators]
  (merge generators
    {::user/id    #(s/gen ::sane-email-address generators)
     ::user/avatar-url #(s/gen ::sane-avatar-url generators)
     ::user/full-name  sane-full-name}))

(defn with-insane-user-generators
  [generators]
  (merge generators
    {::user/id    #(s/gen ::insane-email-address generators)
     ::user/avatar-url #(s/gen ::insane-avatar-url generators)
     ::user/full-name  insane-full-name}))

(defn with-static-user-generators
  [generators]
  (merge generators
    {::user/id    #(sgen/return "jesse.doe@mail.example.com")
     ::user/avatar-url #(sgen/return "https://picsum.photos/20/20/?random")
     ::user/full-name  #(sgen/return "Jesse Doe")}))

(defn with-codemash-user-generators
  [generators]
  (merge generators
    {::user/id
      #(sgen/fmap string/join
          (sgen/tuple
            (sgen/return "Hello.CodeMash")
            (sgen/return "@")
            (sgen/string-alphanumeric)
            (s/gen #{".com" ".us" ".co" ".org"})))}))

;; gratitude.expression generators

(defn with-thank-you-note-generators
  [{:keys [::user/id] :as generators}]
  (merge generators
    {::expression/to
      #(s/gen (s/coll-of ::user/id :max-count 1 :into #{}) generators)
     ::expression/from
      (or id #(s/gen ::user/id generators))
     ::expression/tag
      #(s/gen #{"Assistance" "Kindness" "Mentorship" "Grooviness" "Coolness"})
     ::expression/message
      #(sgen/fmap (partial string/join "  ")
          (sgen/tuple
            (s/gen #{"Great question!" "What a cool idea!" "Way to make me think!"})
            (s/gen #{"This conference is better because of you."
                     "I feel like there are a million new ideas swimming in my mind."
                     "Thanks for all you do."})))}))

;; REPL examples
(comment

  (sgen/generate (s/gen ::user/id))
  (sgen/generate (s/gen ::expression/thank-you-note))

  (println "Sane user thank-you-note")
  (clojure.pprint/pprint
    (sgen/sample
      (s/gen ::expression/thank-you-note
        (-> {}
          with-thank-you-note-generators
          with-sane-user-generators
          with-codemash-user-generators))
      3))

  (println "Insane user thank-you-note")
  (clojure.pprint/pprint
    (sgen/sample
      (s/gen ::expression/thank-you-note
        (-> {}
          with-thank-you-note-generators
          with-insane-user-generators))
      3))

  (clojure.pprint/pprint (sgen/generate (s/gen ::expression/thank-you-note)))

  :end-of-example)
