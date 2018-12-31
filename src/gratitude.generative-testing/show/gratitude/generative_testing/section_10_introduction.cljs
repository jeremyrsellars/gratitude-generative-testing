(ns gratitude.generative-testing.section-10-introduction
  (:require [clojure.string :as string]
            [devcards.core :refer [defcard deftest]]
            devcards.util.markdown
            devcards.system
            [sablono.core :as sab]
            gratitude.generative-testing.core
            [us.sellars.slides.higlight-js :refer [schedule-code-highlighting]])
  (:require-macros [gratitude.doc.core]))

(def slides (gratitude.doc.core/slide-markdown-cards "10_introduction.md"))
