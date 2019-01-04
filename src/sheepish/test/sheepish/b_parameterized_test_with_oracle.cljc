(ns sheepish.b-parameterized-test-with-oracle
  (:require [clojure.test :refer [deftest testing is]]
            [sheepish.core :refer [sheep-bleat?]]))

(defn assert-sheep-bleat
  [text reason]
     ; ^ Remove the correct answer; we have to compute it somehow.
  (is (= (some? (re-find #"^baa+$" text))  ; true when a match is found, false when nil is returned.
         (sheep-bleat? text))
    reason))

(def examples
 [["b"         "Too short"]
  ["ba"        "Too short"]
  ["baa"       "2+ 'a' s"]
  ["baaa"      "2+ 'a' s"]
  ["baaad"     "invalid 'd'"]
  [" baa"      "leading space"]
  ["baa "      "trailing space"]])

(deftest Testing_sheep-bleat?_with_oracle
  ; Run the test for each of the examples
  (doseq [[text reason] examples]
    (assert-sheep-bleat text reason)))
