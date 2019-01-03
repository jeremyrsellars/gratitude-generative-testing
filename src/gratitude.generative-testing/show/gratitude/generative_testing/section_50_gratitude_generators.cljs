(ns gratitude.generative-testing.section-50-gratitude-generators
  (:require [clojure.string :as string]
            [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [devcards.core :refer [defcard deftest]]
            devcards.util.markdown
            devcards.system
            [sablono.core :as sab]
            [us.sellars.slides.higlight-js :refer [schedule-code-highlighting]]
            [gratitude.awards-app.mock_ui]
            [gratitude.data-generators]
            [gratitude.award-generators :as award-gen]
            [gratitude.expression :as expression]
            [gratitude.user :as user])
  (:require-macros [gratitude.doc.core]))

(def slides (gratitude.doc.core/slide-markdown-cards "50_gratitude_generators.md"))

(defcard Vanilla_users
  "
```clojure
  (gen/sample
    (s/gen ::user/user)
    1)
```

  Often produces an error:
   `Error: Unable to construct gen at: [:gratitude.user/avatar-url] for: :gratitude.user/avatar-url]`
  Because usually the randomly-generated avatar-url string won't match the regex."
  (try
    (doall
      (gen/sample
        (s/gen ::user/user)
        4))
    (catch js/Error error
      error)))

(defcard Sane_users
  "```clojure
  (gen/sample
    (s/gen ::user/user
      {::user/id    #(s/gen ::sane-email-address generators)
       ::user/avatar-url #(s/gen ::sane-avatar-url generators)
       ::user/full-name  sane-full-name})
    5)
```"
  (gen/sample
    (s/gen ::user/user
      ;{::user/avatar-url #(gen/return "http://somegth.com/asdf/")}#_
      (-> {}
          gratitude.data-generators/with-sane-user-generators))
    5))

(defcard Insane_users
  "```clojure
  (gen/sample
    (s/gen ::user/user
      (-> {}
          gratitude.data-generators/with-insane-user-generators))
    10)
```"
  (gen/sample
    (s/gen ::user/user
      (-> {}
          gratitude.data-generators/with-insane-user-generators))
    10))

(defcard Sane_users_thank-you-notes
  "```clojure
  (gen/sample
    (s/gen ::expression/thank-you-note
      (-> {}
          gratitude.data-generators/with-sane-user-generators
          gratitude.data-generators/with-thank-you-note-generators))
    5)
```"
  (gen/sample
    (s/gen ::expression/thank-you-note
      (-> {}
          gratitude.data-generators/with-sane-user-generators
          gratitude.data-generators/with-thank-you-note-generators))
    5))

(defcard Insane_users_thank-you-notes
  "```clojure
  (gen/sample
    (s/gen ::expression/thank-you-note
      (-> {}
          gratitude.data-generators/with-insane-user-generators
          gratitude.data-generators/with-thank-you-note-generators))
    10)
```"
  (gen/sample
    (s/gen ::expression/thank-you-note
      (-> {}
          gratitude.data-generators/with-insane-user-generators
          gratitude.data-generators/with-thank-you-note-generators))
    10))

(defcard CodeMash_users_thank-you-notes
  "```clojure
  (gen/sample
    (s/gen ::expression/thank-you-note
      (-> {}
          gratitude.data-generators/with-sane-user-generators
          gratitude.data-generators/with-codemash-user-generators
          gratitude.data-generators/with-thank-you-note-generators))
    3)
```"
  (gen/sample
    (s/gen ::expression/thank-you-note
      (-> {}
          gratitude.data-generators/with-sane-user-generators
          gratitude.data-generators/with-codemash-user-generators
          gratitude.data-generators/with-thank-you-note-generators))
    3))

(defn exercise-awards
  [app])

(defcard Awards-data-one-scenario
  "```clojure
  (gen/generate
    (s/gen ::award-gen/events
      (-> {}
          gratitude.data-generators/with-insane-user-generators
          gratitude.data-generators/with-thank-you-note-generators)))
  ```"
  (gen/generate
    (s/gen ::award-gen/events
      (-> {}
          gratitude.data-generators/with-insane-user-generators
          gratitude.data-generators/with-thank-you-note-generators))))

(defcard Visualized-awards-data-one-scenario
  "
  ```clojure
            (gen/generate
            (s/gen ::award-gen/events
              (-> {}
                  gratitude.data-generators/with-sane-user-generators
                  (assoc ::user/avatar-url #(gen/fmap (partial str \"https://picsum.photos/36/36/?random&x=\")
                                                      (s/gen int?)))
                  gratitude.data-generators/with-thank-you-note-generators)))
   ```
   The results of the above expression are transformed to HTML to help visualize the events that are occurring."
  (fn [data-atom owner]
    (or (seq @data-atom)
      (do
        (reset! data-atom
          (gen/generate
            (s/gen ::award-gen/events
              (-> {}
                  gratitude.data-generators/with-sane-user-generators
                  (assoc ::user/avatar-url #(gen/fmap (partial str "https://picsum.photos/36/36/?random&x=")
                                                      (s/gen int?)))
                  gratitude.data-generators/with-thank-you-note-generators))))
        @data-atom))
    (sab/html
      [:div
        [:button {:onClick #(reset! data-atom nil)}
          "Regenerate"]
        (gratitude.awards-app.mock-ui/events-visualization @data-atom)]))
  nil)

(defcard Awards-data-sample
  "```clojure
  (gen/sample
    (s/gen ::award-gen/events
      (-> {}
          gratitude.data-generators/with-insane-user-generators
          gratitude.data-generators/with-thank-you-note-generators))
    20)```"
  (gen/sample
    (s/gen ::award-gen/events
      (-> {}
          gratitude.data-generators/with-insane-user-generators
          gratitude.data-generators/with-thank-you-note-generators))
    20))
