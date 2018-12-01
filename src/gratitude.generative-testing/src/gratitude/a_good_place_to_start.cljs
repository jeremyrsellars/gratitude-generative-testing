(ns gratitude.a-good-place-to-start
  (:require [devcards.core :refer [defcard deftest]]
            devcards.util.markdown
            [sablono.core :as sab])
  (:require-macros [gratitude.doc.core]))

(defn highlight-code-blocks
  []
  (set! (.-called (.-initHighlighting js/hljs)) false) ;; prevent short-circuit
  (.initHighlighting js/hljs))

; (defonce _Once_highlight-on-interval
;   (js/setInterval highlight-code-blocks 2000))

(defn- schedule-code-highlighting
  []
  (js/setTimeout highlight-code-blocks 10))

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
