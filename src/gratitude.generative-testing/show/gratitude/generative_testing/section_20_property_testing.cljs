(ns gratitude.generative-testing.section-20-property-testing
  (:require [clojure.string :as string]
            [devcards.core :refer [defcard deftest]]
            devcards.util.markdown
            devcards.system
            [sablono.core :as sab]
            [us.sellars.slides.higlight-js :refer [schedule-code-highlighting]])
  (:require-macros [gratitude.doc.core]))

(def slides (gratitude.doc.core/slide-markdown-cards "20_property_testing.md"))
