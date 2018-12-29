(ns gratitude.storage)

(defprotocol IAppendNote
  (append! [this note]))

(defprotocol IRetrieveNotes
  (retrieve [this criteria]))

(defprotocol IRetrieveRegisteredUsers
  (retrieve-registered-users [this]))

(defprotocol IRegisterUser
  (register-user! [this user]))

