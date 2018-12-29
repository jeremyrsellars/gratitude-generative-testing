(ns gratitude.storage.in-memory
  (:require [clojure.spec.alpha :as s]
            [gratitude.storage :as storage]
            [gratitude.expression :as expression]
            [gratitude.user :as user]))

(defn store
  []
  (let [state (atom {:notes []
                     :registered-users {}})
        user-id-is-registered?
          (fn sender-is-registered? [user-id]
            (contains? (get @state :registered-users) user-id))]
    (reify
      storage/IAppendNote
      (append! [this note]
        (let [note-is-valid?   (s/valid? ::expression/note note)
              user-registered? (user-id-is-registered? (::expression/from note))
              error-code (cond (not note-is-valid?)   :invalid-note
                               (not user-registered?) :unregistered-user)]
          (when-not error-code
            (swap! state update :notes conj note))
          error-code))

      storage/IRetrieveNotes
      (retrieve [this {:keys [min-date] :as criteria}]
        (->> @state
             :notes
             (filter (fn [{:keys [::expression/time-of-record]}]
                        (>= time-of-record min-date)))))

      storage/IRetrieveRegisteredUsers
      (retrieve-registered-users [this]
        (get @state :registered-users))

      storage/IRegisterUser
      (register-user! [this user]
        (let [user-is-valid? (s/valid? ::user/user user)
              user-id (::user/id user)
              user-registered? (user-id-is-registered? user-id)
              error-code (cond (not user-is-valid?)   :invalid-user
                               user-registered?       :already-registered)]
          (when-not error-code
            (swap! state update :registered-users assoc user-id user))
          error-code)))))

