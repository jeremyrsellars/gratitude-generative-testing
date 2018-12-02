(ns gratitude.callout.software-craftsmanship
  (:require [devcards.core :refer [defcard deftest]]
            devcards.util.markdown
            [sablono.core :as sab]
            [us.sellars.slides.higlight-js :refer [schedule-code-highlighting]])
  (:require-macros [gratitude.doc.core]))

(defcard Software_Craftsmanship_Manifesto
  (sab/html [:div {:dangerouslySetInnerHTML {"__html" (devcards.util.markdown/markdown-to-html (gratitude.doc.core/softwarecraftsmanship))}}])
  {}
  {:object {:render (schedule-code-highlighting)}
   :classname "black"})

