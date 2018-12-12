(ns gratitude.expression
  (:require [clojure.spec.alpha :as s]))

(s/def ::user-id string?)
(s/def ::tag string?)

(s/def ::message string?)
(s/def ::cheers pos-int?)
(s/def ::from ::user-id)
(s/def ::to (s/coll-of ::user-id :min-count 1))
(s/def ::tags (s/coll-of ::tag))
(s/def ::time-of-record inst?)

(s/def ::thank-you-note
  (s/keys :req [::from ::to ::message ::cheers ::tags ::time-of-record]))

