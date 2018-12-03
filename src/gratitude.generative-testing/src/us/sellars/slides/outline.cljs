(ns us.sellars.slides.outline
  (:require [devcards.core :refer [defcard deftest]]
            devcards.util.markdown
            [sablono.core :as sab]))

(defn- scroll-center
  [evt]
  (.scrollIntoView (.-target evt)
   #js{:behavior "auto"
       :block "center"
       :inline "center"}))

(defn scroll-chamber
  [links]
  [:div#scroll-chamber.scroll-chamber
   [:div#container
    [:div#content
      (for [[idx [text link]] (map-indexed vector links)]
        (let [href (if (string? link) link (:href link))
              attrs (-> (if (string? link) {:href link} link)
                        (assoc :onClick scroll-center
                               :id (or (subs href 1) (str idx))))]

          [:a attrs text]))]]])
