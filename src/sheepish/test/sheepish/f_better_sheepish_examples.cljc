(ns sheepish.f-better-sheepish-examples
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [clojure.string :as string]
            [clojure.test :as test :refer [deftest testing is]]
            clojure.test.check.generators #_ "This is necessary at runtime"))

(defn generate-examples
  [spec example-count]
  (let [generator (s/gen spec)
        generate-example #(gen/generate generator)
        examples (repeatedly generate-example)]
    (take example-count examples)))

;; Let's say we want to compose the test string into before, the `b`, intermediate characters, `a` characters, and "after".

(s/def ::usually-empty-string
  (s/with-gen string?
   #(gen/frequency [[9 (gen/return "")]
                    [1 (s/gen string?)]])))

; (generate-examples ::usually-empty-string 40)

(s/def ::string-of-a
  (s/with-gen string?
    #(gen/fmap
      (fn [n] (string/join (repeat (max n 0) \a)))
      (s/gen (s/int-in -3 4)))))

(s/def ::string-of-b
  (s/with-gen string?
    #(gen/fmap
      (fn [n] (string/join (repeat (max n 0) \b)))
      (s/gen (s/int-in 0 3)))))

; (generate-examples ::string-of-a 40)

(s/def ::sheepish-like-substrings
  (s/with-gen
    (s/coll-of any?)
    #(gen/tuple (s/gen ::usually-empty-string)
                (s/gen ::string-of-b)
                (s/gen ::usually-empty-string)
                (s/gen ::string-of-a)
                (s/gen ::usually-empty-string)
                (s/gen ::string-of-a))))

; (generate-examples ::sheepish-like-substrings 10)

(s/def ::sheepish-like-string
  (s/with-gen
    string?
    #(gen/fmap string/join (s/gen ::sheepish-like-substrings))))

; (generate-examples ::sheepish-like-string 10)

