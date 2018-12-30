(ns gratitude.generative-testing.section-40-composing-generators
  (:require [clojure.string :as string]
            [devcards.core :refer [defcard deftest]]
            devcards.util.markdown
            devcards.system
            [sablono.core :as sab]
            [us.sellars.slides.higlight-js :refer [schedule-code-highlighting]])
  (:require-macros [gratitude.doc.core]))

(defn slide-markdown-to-html
  [markdown]
  (let [replaced-md (string/replace markdown #"\n(-{5,}\r?\n)(?=# )" "\r\n-------------\r\n")]
    (sab/html
      [:div {:dangerouslySetInnerHTML {"__html" (devcards.util.markdown/markdown-to-html replaced-md)}}])))

(defcard _40_composing_generators
  (slide-markdown-to-html (gratitude.doc.core/slide-markdown "40_composing_generators.md"))
  {}
  {:object {:render (schedule-code-highlighting)}})
