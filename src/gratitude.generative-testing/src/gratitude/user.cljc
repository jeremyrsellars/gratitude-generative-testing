(ns gratitude.user
  (:require [clojure.string :as string]
            [clojure.spec.alpha :as s]))

(def avatar-url-regex #"(?i)https?://[-a-zA-Z0-9@:%._\\+~#=]+\.[a-z]+[-a-zA-Z0-9@:%_\\+.~#?&//=]*.*")

(s/def ::avatar-url #(re-matches avatar-url-regex %))
(s/def ::id string?)
(s/def ::full-name string?)

(s/def ::user
  (s/keys :req [::id ::full-name]
          :opt [::avatar-url]))
