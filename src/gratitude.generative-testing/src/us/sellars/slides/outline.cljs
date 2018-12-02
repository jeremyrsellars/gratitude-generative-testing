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
      (for [[text link] links]
        (let [attrs (-> (if (string? link) {:href link} link)
                        (assoc :onClick scroll-center))] ;#(this-as this (js/scrollCenter this))))]
          [:a attrs text]))]]])
