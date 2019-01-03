# Pulling it all together

<style class='before-speaker-note'></style>

To-do: pull it all together.

* [Some generators](#!/gratitude.generative_testing.section_50_gratitude_generators)

-----------

--------
#!/gratitude.generative_testing.section_50_gratitude_generators/Sane_users

--------
#!/gratitude.generative_testing.section_50_gratitude_generators/Insane_users

--------

# User generation

```clojure
(ns gratitude.data-generators (comment "references elided to fit on slide"))

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
```

-----------

# User generation (User id/Email)

```clojure
(s/def ::sane-email-address
 (s/with-gen string? #(sgen/fmap string/join
                        (sgen/tuple
                            (sgen/such-that seq (sgen/string-alphanumeric))
                            (sgen/return "@")
                            (sgen/such-that seq (sgen/string-alphanumeric))
                            (s/gen #{".com" ".us" ".co" ".org"}))))))
(s/def ::insane-email-address
 (s/with-gen string? #(sgen/fmap string/join
                        (sgen/tuple
                            (s/gen string?)
                            (s/gen #{"@" " "})
                            (s/gen string?)
                            (sgen/return ".")
                            (s/gen string?))))))
(s/def ::user-id
 (s/with-gen string? #(sgen/frequency
                        [[9 (s/gen ::sane-email-address)]
                         [1 (s/gen ::insane-email-address)]]))))
```

-----------

# User generation (person name)

```clojure
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
```

-----------

# User generation (full name)

```clojure
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
```

-----------

# User generation (mix-in)

```clojure
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
```

-----------

# User generation (mixins)

```clojure
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
```

-----------

# User spec definition

```clojure
(ns gratitude.user
  (:require [clojure.string :as string]
            [clojure.spec.alpha :as s]))

(def avatar-url-regex #"(?i)https?://[-a-zA-Z0-9@:%._\\+~#=]+\.[a-z]+[-a-zA-Z0-9@:%_\\+.~#?&//=]*.*")

(s/def ::avatar-url #(re-matches avatar-url-regex %))
(s/def ::id string?)
(s/def ::full-name string?)

(s/def ::user
  (s/keys :req [::id ::full-name]
          :opt [::avatar-url]))
```

--------#Sane_users

--------#Insane_users

--------
# Thank you notes definition

```clojure
(ns gratitude.expression  (comment "references elided to fit on slide"))

(s/def ::tag string?)

(s/def ::message string?)
(s/def ::cheers pos-int?)
(s/def ::from ::user/id)
(s/def ::to (s/coll-of ::user/id :min-count 1))
(s/def ::tags (s/coll-of ::tag :into #{}))
(s/def ::time-of-record inst?)

(s/def ::thank-you-note
  (s/keys :req [::from ::to ::message ::cheers ::tags ::time-of-record]))
```

----------

# Thank you notes samples

```clojure
  (sgen/sample
    (s/gen ::user/user
      (-> {}
          gratitude.data-generators/with-insane-user-generators))
    10) ; insane users

  (sgen/sample
    (s/gen ::user/user
      ;{::user/avatar-url #(sgen/return "http://somegth.com/asdf/")}#_
      (-> {}
          gratitude.data-generators/with-sane-user-generators))
    5) ; sane users
```

--------
#!/gratitude.generative_testing.section_50_gratitude_generators/Sane_users_thank-you-notes

--------
#!/gratitude.generative_testing.section_50_gratitude_generators/Insane_users_thank-you-notes

--------
#!/gratitude.generative_testing.section_50_gratitude_generators/CodeMash_users_thank-you-notes

-----------

# Awards system testing (sequence of events)

```clojure
(ns gratitude.award-generators (comment "references elided to fit on slide")))

(s/def ::interval pos-int?)

;; specs for events
(s/def ::delay (s/keys :req [::interval]))
(s/def ::new-user (s/keys :req [::user/user]))
(s/def ::new-note ::expression/thank-you-note)
(s/def ::generate-report #{:generate-report})

(s/def ::event (s/or ::delay ::delay,
                     ::new-user ::new-user
                     ::new-note ::new-note
                     ::generate-report ::generate-report))
(s/def ::events (s/coll-of ::event))

;(sgen/sample (s/gen ::events))
```
--------#Awards-data
