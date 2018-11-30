(ns gratitude.generative-testing.core
  (:require [devcards.core :refer [defcard deftest]]
            [sablono.core :as sab]
            gratitude.a-good-place-to-start)
  (:require-macros [gratitude.doc.core]))

(defcard About_Devcards
  (sab/html
    [:div
      [:h2 "Devcards + Figwheel makes development a bit more visual and interactive."]
      [:h3 [:a {:href "https://github.com/bhauman/devcards"} "Devcards"] " – You are here"]
      [:p "You've probably noticed that your browser is in an environment called 'devcards'.   This shows some tests, examples, and documentation.  It can be useful as a tour through select parts of the system.  When running figwheel, the browser will update every time you save a code file."]
      [:h3 [:a {:href "https://github.com/bhauman/lein-figwheel"} "Figwheel"] " – Live reloading when you save a code file"]
      [:p "When running figwheel, the browser will update every time you save a code file.  This makes some development activities a lot easier.  Note, this only affects attached browser windows."]
      [:h3 [:a {:href "#"} "Other things to see"]]
      [:p "Below is the README.md file for the project.  Other tests, examples, and documentation can be found " [:a {:href "#"} "here"] "."]]))

(defcard README.md
  (sab/html [:div {:dangerouslySetInnerHTML {"__html" (devcards.util.markdown/markdown-to-html (gratitude.doc.core/readme))}}]))

(defcard TODO.md
  (sab/html [:div {:dangerouslySetInnerHTML {"__html" (devcards.util.markdown/markdown-to-html (gratitude.doc.core/todo))}}]))

(defn main []
  ;; conditionally start the app based on whether the #main-app-area
  ;; node is on the page
  (if-let [node (.getElementById js/document "main-app-area")]
    (.render js/ReactDOM (sab/html [:div "This is working"]) node)))

(main)

;; remember to run lein figwheel and then browse to
;; http://localhost:3449/cards.html

