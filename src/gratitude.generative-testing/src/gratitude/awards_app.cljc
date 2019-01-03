(ns gratitude.awards-app
  (:require [clojure.spec.alpha :as s]
            [gratitude.expression :as expression]
            [gratitude.user :as user]
            [gratitude.data-entry :as data-entry]
            [gratitude.storage :as storage]
            gratitude.storage.in-memory))

(defprotocol IAwardsApp
  (process-event! [this note])
  (send! [this note]))

(defn in-memory-app
  []
  (let [storage-repo (gratitude.storage.in-memory/store)]
    (reify
      storage/IAppendNote
      (append! [this note]
        (storage/append! storage-repo note))

      storage/IRetrieveNotes
      (retrieve [this criteria]
        (storage/retrieve storage-repo criteria))

      storage/IRetrieveRegisteredUsers
      (retrieve-registered-users [this]
        (storage/retrieve-registered-users storage-repo))

      storage/IRegisterUser
      (register-user! [this user]
        (storage/register-user! storage-repo user)))))


(defn exercise-awards
  [app events])


