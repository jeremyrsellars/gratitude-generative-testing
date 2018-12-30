(ns gratitude.a-good-place-to-start
  (:require [clojure.string :as string]
            [devcards.core :refer [defcard deftest]]
            devcards.util.markdown
            devcards.system
            [sablono.core :as sab]
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

(def outline
  [["Welcome"                1    "#!/gratitude.a_good_place_to_start/Welcome"]
   ["Introduction"           1    "#!/gratitude.generative_testing.section_10_introduction"]
   ["Property testing"       0    "#!/gratitude.generative_testing.section_20_property_testing"]
   ["Simple generators"      0    "#!/gratitude.generative_testing.section_30_simple_generators"]
   ["Composing generators"   0    "#!/gratitude.generative_testing.section_40_composing_generators"]
   ["Gratitude app demo"     0    "#!/gratitude.generative_testing.section_50_gratitude_generators"]
   ["Closing"                1    "#!/gratitude.generative_testing.section_60_closing"]])

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
      (for [[title level href] (next outline #_ :skip-the-title-slide)
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
      (for [[title level href] outline]
        [:li {:key (str title "_" href)}[:a {:href href} title]])]]))

(defcard Software_Craftsmanship_Manifesto_link
  (sab/html [:div [:a {:href "#!/gratitude.callout.software_craftsmanship/Software_Craftsmanship_Manifesto"
                       :target "manifesto"}
                    "Open slide"]
                  [:br]
                  [:a {:href "http://manifesto.softwarecraftsmanship.org"
                       :target "manifesto"}
                    "Open manifesto.softwarecraftsmanship.org"]]))
