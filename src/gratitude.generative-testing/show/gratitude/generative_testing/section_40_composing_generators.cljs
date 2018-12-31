(ns gratitude.generative-testing.section-40-composing-generators
  (:require [clojure.string :as string]
            [devcards.core :refer [defcard deftest]]
            devcards.util.markdown
            devcards.system
            [sablono.core :as sab]
            [us.sellars.slides.higlight-js :refer [schedule-code-highlighting]])
  (:require-macros [gratitude.doc.core]))

(def slides (gratitude.doc.core/slide-markdown-cards "40_composing_generators.md"))
