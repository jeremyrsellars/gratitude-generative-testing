(ns gratitude.generative-testing.core
  (:require [clojure.string :as string]
            [devcards.core :refer [defcard deftest]]
            [sablono.core :as sab]
            us.sellars.slides.outline
            [goog.events :as events :refer [EventType]]
            [goog.object :as obj]
            gratitude.callout.software-craftsmanship
            gratitude.app)
  (:require-macros [gratitude.doc.core]))

(def outline
  [["Welcome"                1    "#!/gratitude.a_good_place_to_start/Welcome"]
   ["Introduction"           1    "#!/gratitude.generative_testing.section_10_introduction"]
   ["Property testing"       0    "#!/gratitude.generative_testing.section_20_property_testing"]
   ["Simple generators"      0    "#!/gratitude.generative_testing.section_30_simple_generators"]
   ["Composing generators"   0    "#!/gratitude.generative_testing.section_40_composing_generators"]
   ["Gratitude app demo"     0    "#!/gratitude.generative_testing.section_50_gratitude_generators"]
   ["Closing"                1    "#!/gratitude.generative_testing.section_60_closing"]])

(def slide-outline
  [{:description "Welcome"
    :hash "#!/gratitude.a_good_place_to_start/Welcome"}])

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

(def outline-atom
  (atom
   (into
    [["Welcome"
      "#!/gratitude.a_good_place_to_start/Welcome"]
     ["Gratitude for Sponsors"
      "#!/gratitude.a_good_place_to_start/Gratitude_for_sponsors"]
     ["Craftsmanship"
      "#!/gratitude.a_good_place_to_start/Software_Craftsmanship_Manifesto_link"]
     ["TODO.md"
      "#!/gratitude.a_good_place_to_start/TODO.md"]
     ["TODO.md (bad)"
      "#!/gratitude.generative_testing.core/TODO.md"]
     ["gratitude.generative_testing.core"
      "#!/gratitude.generative_testing.core"]]
    (map (juxt first #(nth % 2))
      outline))))

(defn ^:export nextSlideHash
  [hash]
  (let [hashs (map second @outline-atom)
        associated-slide (zipmap hashs (next hashs))]
    (get associated-slide hash)))

(defn ^:export previousSlideHash
  [hash]
  (let [hashs (map second @outline-atom)
        associated-slide (zipmap (next hashs) hashs)]
    (get associated-slide hash)))

(defn ^:export homeSlideHash
  [hash]
  (let [hashs (map second @outline-atom)]
    (first hashs)))

(defn ^:export endSlideHash
  [hash]
  (let [hashs (map second @outline-atom)]
    (last hashs)))

(def navigation-commands
  {"p" previousSlideHash
   "n" nextSlideHash
   "c" identity})
   ; "h" homeSlideHash
   ; "e" endSlideHash})

(defn ^:export main [element-id]
  ;; conditionally start the app based on whether the #main-app-area
  ;; node is on the page
  (if-let [node (.getElementById js/document element-id)]
    (do
      (events/listen js/window "hashchange"
        (fn handle-hashchange [evt]
          (let [hash (-> js/window .-location .-hash)
                id   (and (> (count hash) 1) (subs hash 1))
                elem (and node id (.getElementById js/document hash))]
            (when elem
              (.scrollIntoView elem
               #js{:behavior "auto"
                   :block "center"
                   :inline "center"})))))

      (events/listen (.-body js/document) (.-KEYPRESS EventType)
        (fn handle-keypress [evt]
          (let [new-hash-fn
                  (get navigation-commands
                       (string/lower-case (.fromCharCode js/String (.-charCode evt)))
                       (constantly nil))]
            (when-let [new-hash (-> js/window .-location .-hash new-hash-fn)]
              (obj/set (.-location js/window) "hash" new-hash)
              (.scrollTo js/window 0 0)))))
      (.render js/ReactDOM (sab/html (us.sellars.slides.outline/scroll-chamber @outline-atom)) node))
    (println "outline element wasn't found")))

(defn ^:export startSlides
  []
  (println "Starting devcards")
  (devcards.core/start-devcard-ui!)
  (println "Started devcards"))

;; remember to run lein figwheel and then browse to
;; http://localhost:3470/cards.html

