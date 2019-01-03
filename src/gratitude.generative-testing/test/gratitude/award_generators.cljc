(ns gratitude.award-generators
  (:require [clojure.pprint :as pprint]
            [clojure.string :as string]
            [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [gratitude.user :as user]
            [gratitude.expression :as expression]
            [clojure.test.check.generators]))

;; Specs for sequences of events to test an awards system.

(s/def ::interval pos-int?)

;; specs for events
(s/def ::delay (s/keys :req [::interval]))
(s/def ::new-user (s/keys :req [::user/user]))
(s/def ::new-note ::expression/thank-you-note)
(s/def ::generate-report #{:generate-report})

(def events
  #{::delay ::new-user ::new-note ::generate-report})

(s/def ::event (s/or ::delay ::delay,
                     ::new-user ::new-user
                     ::new-note ::new-note
                     ::generate-report ::generate-report))
(s/def ::events (s/coll-of ::event))

;(gen/sample (s/gen ::events))
