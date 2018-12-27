(ns gratitude.generative-testing.section-30-simple-generators
  (:require [clojure.string :as string]
            [devcards.core :refer [defcard deftest]]
            devcards.util.markdown
            devcards.system
            [sablono.core :as sab]
            [us.sellars.slides.higlight-js :refer [schedule-code-highlighting]])
  (:require-macros [gratitude.doc.core]))

(defn slide-markdown-to-html
  [markdown]
  (let [replaced-md (string/replace markdown #"(?<=\n)(-{5,}\r?\n)(?=# )" "\r\n-------------\r\n")]
    (sab/html
      [:div {:dangerouslySetInnerHTML {"__html" (devcards.util.markdown/markdown-to-html replaced-md)}}])))

#_
(defcard _10_introduction
  (slide-markdown-to-html (gratitude.doc.core/slide-markdown "10_introduction.md"))
  {}
  {:object {:render (schedule-code-highlighting)}})

#_
(defcard _20_property_testing
  (slide-markdown-to-html (gratitude.doc.core/slide-markdown "20_property_testing.md"))
  {}
  {:object {:render (schedule-code-highlighting)}})

(defcard _30_simple_generators
  (slide-markdown-to-html (gratitude.doc.core/slide-markdown "30_simple_generators.md"))
  {}
  {:object {:render (schedule-code-highlighting)}})

#_
(defcard _40_composing_generators
  (slide-markdown-to-html (gratitude.doc.core/slide-markdown "40_composing_generators.md"))
  {}
  {:object {:render (schedule-code-highlighting)}})

