(ns gratitude.app
  (:require [devcards.core :refer [defcard deftest]]
            gratitude.data-generators
            [sablono.core :as sab]
            gratitude.data-entry
            gratitude.expression
            gratitude.storage
            gratitude.data-entry.impl
            gratitude.storage.in-memory))

(defn registration-ui
  [{:keys [full-name id avatar-url]}]
  [:div.registration
   [:h1 "It is nice to meet you!"]
   [:form
     [:div "Avatar"
      [:img {:src avatar-url}]
      (if (some? avatar-url)
        [:label {:for "avatar-url"}
          [:input#avatar-url {:value (or avatar-url "")
                              :placeholder "http://your.domain/image"}]])]
     [:label {:for "id"}
      [:div "Email address"]
      [:input#id {:value (or id "")
                  :placeholder "your.email@example.com"}]]
     [:label {:for "name"}
      [:div "Full name"]
      [:input#name {:value (or full-name "")
                    :placeholder "General Greatful"}]]
     [:input {:type "submit"}]]])

(defn data-entry-ui
  []
  [:div.data-entry
   [:h1 "Gratitue Rocks!"]
   [:form
     [:label {:for "to"}
      [:div "To"]
      [:input#to]]
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
     [:label {:for "tags"}
      [:div "Tags"]
      [:input#tags]]
     [:input {:type "submit"}]]])

(defn registration-ui-mock-up
  []
  [:div
    (registration-ui {})
    [:hr]
    (registration-ui {:id "jo@example.com"
                      :full-name "The Jo Sen Wan"
                      :avatar-url "https://picsum.photos/128/128/?random&x="})
    [:hr]
    (data-entry-ui)])

(defn ui-mock-up
  []
  [:div
    (data-entry-ui)])

(defn ^:export renderRegistration
  [element-id]
  (if-let [node (.getElementById js/document element-id)]
    (.render js/ReactDOM (sab/html (registration-ui-mock-up)) node)
    (println element-id "element wasn't found")))

(def renderDataEntry-element (atom nil))
(defn ^:export renderDataEntry
  [element-id]
  (reset! renderDataEntry-element element-id)
  (if-let [node (.getElementById js/document element-id)]
    (.render js/ReactDOM (sab/html (ui-mock-up)) node)
    (println element-id "element wasn't found")))

(defn reload-hook
  []
  (println "reloading")
  (when-let [element-id @renderDataEntry-element]
    (renderDataEntry element-id)))
