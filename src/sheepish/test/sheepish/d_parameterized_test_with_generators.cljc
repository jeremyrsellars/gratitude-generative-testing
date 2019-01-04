(ns sheepish.d-parameterized-test-with-properties
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [clojure.string :as string]
            [clojure.test :as test :refer [deftest testing is]]
            clojure.test.check.generators #_ "This is necessary at runtime"))

;; Make a generator for int values
; user=> (s/gen int?)
; #clojure.test.check.generators.Generator{:gen #object[clojure.test.check.generators$such_that$fn__1825 0x633837ae "clojure.test.check.generators$such_that$fn__1825@633837ae"]}
; user=> (gen/generate (s/gen int?))
; 123  ; or some integer
; user=> (take 3 (repeatedly #(gen/generate (s/gen int?))))
; (60273 -94 -3)   ; 3 examples

(defn generate-examples
  [spec example-count]
  (let [generator (s/gen spec)
        generate-example #(gen/generate generator)
        examples (repeatedly generate-example)]
    (take example-count examples)))

;; Some examples with built-in generators
(generate-examples int? 10)   ; Generate 10 integers
(generate-examples string? 3) ; 3 strings
(generate-examples nil? 20)   ; some nils

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
