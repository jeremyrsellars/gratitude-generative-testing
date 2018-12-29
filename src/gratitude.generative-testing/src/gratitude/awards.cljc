(ns gratitude.awards
  (:require [clojure.spec.alpha :as s]
            [gratitude.expression :as expression]
            [gratitude.user :as user]
            [gratitude.data-entry :as data-entry]))

(defn generate-report
  [{:keys [registered-users notes]}]
  (->> notes
       (group-by ::user/id)
       (keep (fn [[user-id user-notes]]
                (when-let [user (registered-users user-id)]
                  {:user-id    user-id
                   :user       user
                   :user-notes user-notes
                   :cheers     (reduce + 0 (map ::expression/cheers user-notes))})))
       (into (sorted-map-by (comp - :cheers)))))
