(ns gratitude.generative-testing.section-50-gratitude-generators
  (:require [clojure.string :as string]
            [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as sgen]
            [devcards.core :refer [defcard deftest]]
            devcards.util.markdown
            devcards.system
            [sablono.core :as sab]
            [us.sellars.slides.higlight-js :refer [schedule-code-highlighting]]
            [gratitude.data-generators]
            [gratitude.award-generators :as award-gen]
            [gratitude.expression :as expression]
            [gratitude.user :as user])
  (:require-macros [gratitude.doc.core]))

(def slides (gratitude.doc.core/slide-markdown-cards "50_gratitude_generators.md"))

(defcard Sane_users
  (sgen/sample
    (s/gen ::user/user
      ;{::user/avatar-url #(sgen/return "http://somegth.com/asdf/")}#_
      (-> {}
          gratitude.data-generators/with-sane-user-generators))
    5))

(defcard Insane_users
  (sgen/sample
    (s/gen ::user/user
      (-> {}
          gratitude.data-generators/with-insane-user-generators))
    10))

(defcard Sane_users_thank-you-notes
  (sgen/sample
    (s/gen ::expression/thank-you-note
      (-> {}
          gratitude.data-generators/with-sane-user-generators
          gratitude.data-generators/with-thank-you-note-generators))
    5))

(defcard Insane_users_thank-you-notes
  (sgen/sample
    (s/gen ::expression/thank-you-note
      (-> {}
          gratitude.data-generators/with-insane-user-generators
          gratitude.data-generators/with-thank-you-note-generators))
    10))

(defcard CodeMash_users_thank-you-notes
  (sgen/sample
    (s/gen ::expression/thank-you-note
      (-> {}
          gratitude.data-generators/with-sane-user-generators
          gratitude.data-generators/with-codemash-user-generators
          gratitude.data-generators/with-thank-you-note-generators))
    3))

(defcard Awards-data
  (sgen/sample
    (s/gen ::award-gen/events
      (-> {}
          gratitude.data-generators/with-insane-user-generators
          gratitude.data-generators/with-thank-you-note-generators))
    20))
