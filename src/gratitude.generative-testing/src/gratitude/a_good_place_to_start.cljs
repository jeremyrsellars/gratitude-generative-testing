(ns gratitude.a-good-place-to-start
  (:require [devcards.core :refer [defcard deftest]]
            devcards.util.markdown
            devcards.system
            [sablono.core :as sab]
            [us.sellars.slides.higlight-js :refer [schedule-code-highlighting]])
  (:require-macros [gratitude.doc.core]))

#_
(def _alter-devcard-defaults
  (do
    (swap! devcards.system/app-state
      assoc-in [:base-card-options :heading] false)))

(defcard Welcome
  (sab/html [:div
             [:h1 "Generate Data for 10,000 Unit Tests"]
             [:h2 "Jeremy Sellars"]
             [:h2 "@agentJsellars"]
             [:p]
             [:a {:href "http://jeremyrsellars.github.io/no-new-legacy/" :target "_new"}
                "No New Legacy Blog – jeremyrsellars.github.io/no-new-legacy/"]
             [:p]
             [:a {:href "http://softekinc.com" :target "_new"}
                "Softek Solutions – softekinc.com"]])
  {}
  {:heading false})

(defcard Gratitude_for_sponsors)

(defcard README.md
  (sab/html [:div {:dangerouslySetInnerHTML {"__html" (devcards.util.markdown/markdown-to-html (gratitude.doc.core/readme))}}])
  {}
  {:object {:render (schedule-code-highlighting)}})

(defcard TODO.md
  (sab/html [:div {:dangerouslySetInnerHTML {"__html" (devcards.util.markdown/markdown-to-html (gratitude.doc.core/todo))}}])
  {}
  {:object {:render (schedule-code-highlighting)}})

; (defcard Software_Craftsmanship_Manifesto
;   (sab/html [:div {:id "!/gratitude.a_good_place_to_start/Software_Craftsmanship_Manifesto"}
;              [:iframe {:src "http://manifesto.softwarecraftsmanship.org"}]])
;                        ;:style {:width "100%", :height "100%" :position "fixed", :left "10%", :right "10%", :top "10%", :bottom "10%"}}]]))
;   {}
;   {:classname "black"})

; (defcard Software_Craftsmanship_Manifesto_Internal
;   (sab/html [:div
;              [:iframe {:src "#!/gratitude.callout.software_craftsmanship/Software_Craftsmanship_Manifesto"
;                        :style {:width "50vw", :height "50vh"}}]])
;   {}
;   {:classname "black"})

; (defcard Software_Craftsmanship_Manifesto
;   (sab/html [:iframe {:dangerouslySetInnerHTML {"__html" (devcards.util.markdown/markdown-to-html (gratitude.doc.core/softwarecraftsmanship))}
;                       :style {:width "50vw", :height "50vh"}}])
;   {}
;   {:classname "black"})



(defcard Software_Craftsmanship_Manifesto_link
  (sab/html [:div [:a {:href "#!/gratitude.callout.software_craftsmanship/Software_Craftsmanship_Manifesto"
                       :target "manifesto"}
                    "Open slide"]
                  [:br]
                  [:a {:href "http://manifesto.softwarecraftsmanship.org"
                       :target "manifesto"}
                    "Open manifesto.softwarecraftsmanship.org"]]))
