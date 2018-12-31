(ns gratitude.a-good-place-to-start
  (:require [clojure.string :as string]
            [devcards.core :refer [defcard deftest]]
            devcards.util.markdown
            devcards.system
            [sablono.core :as sab]
            gratitude.generative-testing.core
            gratitude.generative-testing.section-10-introduction
            gratitude.generative-testing.section-20-property-testing
            gratitude.generative-testing.section-30-simple-generators
            gratitude.generative-testing.section-40-composing-generators
            gratitude.generative-testing.section-50-gratitude-generators
            gratitude.generative-testing.section-60-closing
            [us.sellars.slides.higlight-js :refer [schedule-code-highlighting]])
  (:require-macros [gratitude.doc.core]))

#_
(def _alter-devcard-defaults
  (do
    (swap! devcards.system/app-state
      assoc-in [:base-card-options :heading] false)))

(defcard Welcome
  (sab/html
   [:div
             [:h1 "Generate Data for 10,000 Unit Tests"]
             [:h2 "Jeremy Sellars"]
             [:h2 "@agentJsellars"]
             [:p]
             [:a {:href "http://jeremyrsellars.github.io/no-new-legacy/" :target "_new"}
                "No New Legacy Blog – jeremyrsellars.github.io/no-new-legacy/"]
             [:p]
             [:a {:href "http://softekinc.com" :target "_new"}
                "Softek Solutions – softekinc.com"]
    [:h2 "Outline"]
    [:ul
      (for [[title level href] (next gratitude.generative-testing.core/outline #_ :skip-the-title-slide)
            :when (zero? level)]
        [:li {:key (str title "_" href)}[:a {:href href} title]])]])
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

(defcard Outline
  (sab/html
   [:div
    [:h1 "Generating data for 10,000 tests"]
    [:ul
      (for [[title level href] gratitude.generative-testing.core/outline]
        [:li {:key (str title "_" href)}[:a {:href href} title]])]]))

(defcard Software_Craftsmanship_Manifesto_link
  (sab/html [:div [:a {:href "#!/gratitude.callout.software_craftsmanship/Software_Craftsmanship_Manifesto"
                       :target "manifesto"}
                    "Open slide"]
                  [:br]
                  [:a {:href "http://manifesto.softwarecraftsmanship.org"
                       :target "manifesto"}
                    "Open manifesto.softwarecraftsmanship.org"]]))

(def _setup-card-sequence
  (reset! gratitude.generative-testing.core/outline-atom
    (reduce
      #(into %1 (map (juxt :description :hash) %2))
      []
      [gratitude.generative-testing.core/slide-outline
       gratitude.generative-testing.section-10-introduction/slides
       gratitude.generative-testing.section-20-property-testing/slides
       gratitude.generative-testing.section-30-simple-generators/slides
       gratitude.generative-testing.section-40-composing-generators/slides
       gratitude.generative-testing.section-50-gratitude-generators/slides
       gratitude.generative-testing.section-60-closing/slides])))
