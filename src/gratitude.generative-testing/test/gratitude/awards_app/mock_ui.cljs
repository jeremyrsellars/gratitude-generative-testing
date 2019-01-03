(ns gratitude.awards-app.mock-ui
  (:require [clojure.string :as string]
            [clojure.spec.alpha :as s]
            [devcards.core :refer [defcard deftest]]
            [sablono.core :as sab]
            [gratitude.awards :as awards]
            [gratitude.award-generators :as award-gen]
            gratitude.data-generators
            [gratitude.expression :as expression]
            [gratitude.user :as user]))

(def the-epoch (js/Date. 0))

(defn add-milliseconds
  ([ms] (add-milliseconds (js/Date.) ms))
  ([d ms] (js/Date. (+ (.getTime d) ms))))

(defmulti event-visualization
  (fn event-visualization-dispatch [[event-type :as conformed-event]]
    event-type))

(defmethod event-visualization :delay
  [[event-type {:keys [::award-gen/interval]}]]
  [:div {:style {:text-align "center"}}
    [:b (str "No activity for " (/ interval 1000) " seconds")]])

(defmethod event-visualization :generate-report
  [[event-type {:keys [::award-gen/interval]}]]
  [:div {:style {:text-align "center"}}
    [:b "Generate report"]])

(defmethod event-visualization :new-user
  [[event-type {:keys [::user/user]}]]
  (let [{:keys [::user/id ::user/full-name ::user/avatar-url]} user]
    [:div {:style {:border "1px solid lightgray"}}
     (when avatar-url
       [:img {:src avatar-url
              :style {:float "left"}}])
     [:b full-name]
     " ("
     [:b id]
     ") joined"]))

(defmethod event-visualization :new-note
  [[event-type {:keys [::expression/tags ::expression/from
                       ::expression/to ::expression/message]}]]
  [:div {:style {:border "1px solid lightgray"
                 :padding-left "3em"}}
   [:div "From:" [:b from]]
   "To:"
   [:ul {:style {:display "inline-block"}}
     (interpose "; "
      (for [user-id to]
        [:li {:style {:display "inline-block"}} user-id]))]
   [:hr]
   [:div
      [:b "Message:"] [:br]
      message]
   [:hr]
   (:ul
      (for [tag tags]
        [:li {:style {:display "inline-block"}} tag]))])

(defmethod event-visualization :default
  [[event-type event]]
  [:pre
    [:code
      (pr-str event)]])

(defn events-visualization
  [events]
  (let [time-adjusted-events
        (:events
          (reduce
            (fn adjust-time
              [{:keys [t events]} {:keys [::award-gen/interval] :as event}]
              (let [new-time (add-milliseconds t (or interval 0))]
                {:t new-time
                 :events (conj events (assoc event :date-of-entry new-time))}))
            {:t (js/Date.)
             :events []}
            events))]
    [:div
     [:hr]
     (interpose [:hr]
      (for [[idx {:keys [date-of-entry] :as event}] (map-indexed vector time-adjusted-events)
            :let [[event-type _ :as conformed] (s/conform ::award-gen/event event)]]
       [:div
        [:span (str (inc idx) ". ")]
        [:span {:style {;:float "left",
                        :background "lightgray" :border "1px solid gray"}}
          (name event-type)]
        (string/replace (str " at " date-of-entry) #" GMT.*" "")
        #_
        [:pre
          [:code
            (pr-str event)]]
        (event-visualization conformed)]))]))
