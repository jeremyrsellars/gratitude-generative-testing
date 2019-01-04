(ns sheepish.e-hearts-card-generators
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [clojure.string :as string]
            [clojure.test :as test :refer [deftest testing is]]
            clojure.test.check.generators #_ "This is necessary at runtime"))

(defn generate-examples
  "Simple function to generate some example values that satisfy a spec."
  [spec example-count]
  (let [generator (s/gen spec)
        generate-example #(gen/generate generator)
        examples (repeatedly generate-example)]
    (take example-count examples)))

(s/def ::suit #{:clubs, :hearts, :spades, :diamonds})
(s/def ::rank #{:king :queen :jack :ten :nine :eight :seven :six :five :four :three :two :ace})
(s/def ::card (s/keys :req-un [::suit ::rank]))

;; Some examples with built-in generators
(generate-examples ::suit 8)   ; Generate 8 suits
(generate-examples ::rank 8)   ; 8 ranks
(generate-examples ::card 3)   ; 3 cards

; user=> (generate-examples ::suit 8)   ; Generate 8 suits
; (:clubs :diamonds :hearts :clubs :clubs :diamonds :diamonds :hearts)
; user=> (generate-examples ::rank 8)   ; 8 ranks
; (:five :nine :six :three :seven :nine :ace :six)
; user=> (generate-examples ::card 3)   ; 3 cards
; ({:suit :diamonds, :rank :ten} {:suit :diamonds, :rank :three} {:suit :diamonds, :rank :queen})

(s/def ::any-string string?)
(generate-examples ::any-string 3)  ; 3 values matching the ::any-string spec

(s/def ::a-or-b #{"a" "b"})
(generate-examples ::a-or-b 20)

(generate-examples ;(s/coll-of #{"a" "b"})
  (s/with-gen string?
    #(gen/fmap string/join (s/gen (s/coll-of #{"a" "b"}))))
  20)

(gen/sample
  (gen/fmap string/join (s/gen (s/coll-of #{"a" "b"})))
  20)

(defn sheep-bleat?
  [s]
  ;(boolean (re-matches #"baa+" s)))
  false) ; obvious bug here!

(defn assert-sheep-bleat
  [text reason]
  (testing (str `("sheep-bleat?" ~text) ": " reason)
    (when (< (count text) 3)
      (is (false? (sheep-bleat? text))
        "Bleat is always at least 3 characters."))
    (when (sheep-bleat? text)
      (is (string/starts-with? text "b")
        "Bleat always starts with `b`")
      (is (string/ends-with? text "aa")
        "Bleat always ends with `aa`")
      (is (= #{\a \b} (into #{} text))
        "Bleat only contains `a` and `b` characters"))
    (when-not (= #{\a \b} (into #{} text))
      (is (false? (sheep-bleat? text))
        "Bleat only contains `a` and `b` characters"))))

(def examples
 (->>
  (gen/sample
    (gen/fmap string/join (s/gen (s/coll-of #{"a" "b"})))
    20)
  distinct
  (map #(vector % "generated"))))

(deftest Testing_sheep-bleat?_with_properties
  ; Run the test for each of the examples
  (doseq [[text reason] examples]
    (assert-sheep-bleat text reason)))


; (test/test-ns *ns*)
