(ns gratitude.storage.in-memory
  (:require [clojure.spec.alpha :as s]
            [gratitude.storage :as storage]
            [gratitude.expression :as expression]))

(defn store
  []
  (let [notes (atom [])]
    (reify
      storage/IAppendNote
      (append! [this note]
        (let [is-valid? (s/valid? ::expression/note note)]
          (when is-valid?
            (swap! notes conj note))
          is-valid?))

      storage/IRetrieveNotes
      (retrieve [this {:keys [min-date] :as criteria}]
        (->> @notes
             (filter (fn [{:keys [::expression/time-of-record]}]
                        (>= time-of-record min-date))))))))
