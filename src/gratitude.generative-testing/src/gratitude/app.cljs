(ns gratitude.app
  (:require [devcards.core :refer [defcard deftest]]
            [sablono.core :as sab]
            gratitude.data-entry
            gratitude.expression
            gratitude.storage
            gratitude.data-entry.impl
            gratitude.storage.in-memory))

(defn data-entry-ui
  []
  [:div.data-entry
   [:h1 "Gratitue Rocks!"]
   [:form
     [:label {:for "message"}
      [:div "Message"]
      [:textarea#message {:rows 10 :cols 80}]]
     [:label {:for "cheers"}
      [:div "Cheers"]
      [:select#cheers {:default-value 1}
        [:option {:key 0 :value 0} "0 Cheers"]
        [:option {:key 1 :value 1} "1 Cheers"]
        [:option {:key 2 :value 2} "2 Cheers"]
        [:option {:key 3 :value 3} "3 Cheers"]]]
     [:label {:for "to"}
      [:div "To"]
      [:input#to]]
     [:label {:for "tags"}
      [:div "Tags"]
      [:input#tags]]
     [:input {:type "submit"}]]])

(def renderDataEntry-element (atom nil))
(defn ^:export renderDataEntry
  [element-id]
  (reset! renderDataEntry-element element-id)
  ;; conditionally start the app based on whether the #main-app-area
  ;; node is on the page
  (if-let [node (.getElementById js/document element-id)]
    (.render js/ReactDOM (sab/html (data-entry-ui)) node)
    (println element-id "element wasn't found")))

(defn reload-hook
  []
  (println "reloading")
  (when-let [element-id @renderDataEntry-element]
    (renderDataEntry element-id)))
