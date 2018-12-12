(ns gratitude.data-entry.impl
  (:require [clojure.spec.alpha :as s]
            [gratitude.expression :as expression]
            [gratitude.data-entry :as data-entry]))

(defn println-writer
  []
  (reify
    data-entry/INoteService
    (invalid-explanation [this note]
      (s/explain-data ::expression/thank-you-note note))

    (send! [this note]
      (println note))))
