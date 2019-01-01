(ns us.sellars.slides.higlight-js)
  ; (:require [devcards.core :refer [defcard deftest]]
  ;           devcards.util.markdown
  ;           [sablono.core :as sab])
  ; (:require-macros [gratitude.doc.core]))

(set! *warn-on-infer* true)

(defn highlight-code-blocks
  []
  (set! (.-called (.-initHighlighting js/hljs)) false) ;; prevent short-circuit
  (.initHighlighting js/hljs))

; (defonce _Once_highlight-on-interval
;   (js/setInterval highlight-code-blocks 2000))

(defn schedule-code-highlighting
  []
  (js/setTimeout highlight-code-blocks 10))
