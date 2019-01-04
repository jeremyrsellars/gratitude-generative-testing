(ns sheepish.a-parameterized-test
  (:require [clojure.test :refer [deftest testing is]]
            [sheepish.core :refer [sheep-bleat?]]))

(defn assert-sheep-bleat
  [text expected-answer reason]
  (is (= expected-answer
         (sheep-bleat? text))
    reason))

(def examples
 [["b"         false    "Too short"]
  ["ba"        false    "Too short"]
  ["baa"       true     "2+ 'a' s"]
  ["baaa"      true     "2+ 'a' s"]
  ["baaad"     false    "invalid ' d'"]
  [" baa"      false    "leading space"]
  ["baa "      false    "trailing space"]])

(deftest Testing_sheep-bleat?
  ; Run the test for each of the examples
  (doseq [[text expected-answer reason] examples]
    (assert-sheep-bleat text expected-answer reason)))
