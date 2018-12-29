(ns gratitude.award-generators
  (:require [clojure.pprint :as pprint]
            [clojure.string :as string]
            [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as sgen]
            [gratitude.user :as user]
            [gratitude.expression :as expression]
            [clojure.test.check.generators]))

(s/def ::delay pos-int?)
(s/def ::new-user ::user/user)
(s/def ::new-note ::expression/thank-you-note)
(s/def ::generate-report #{::generate-report})

(def events
  #{::delay ::new-user ::new-note ::generate-report})

(s/def ::event (s/or ::delay ::delay ::new-user ::new-user ::new-note ::new-note ::generate-report ::generate-report))
(s/def ::events (s/coll-of ::event))

;(sgen/sample (s/gen ::events))
