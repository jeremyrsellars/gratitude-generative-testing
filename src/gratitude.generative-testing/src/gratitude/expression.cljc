(ns gratitude.expression
  (:require [clojure.string :as string]
            [clojure.pprint :as pprint]
            [clojure.spec.alpha :as s]
            [gratitude.user :as user]))

(s/def ::tag string?)

(s/def ::message string?)
(s/def ::cheers pos-int?)
(s/def ::from ::user/id)
(s/def ::to (s/coll-of ::user/id :min-count 1))
(s/def ::tags (s/coll-of ::tag :into #{}))
(s/def ::time-of-record inst?)

(s/def ::thank-you-note
  (s/keys :req [::from ::to ::message ::cheers ::tags ::time-of-record]))
