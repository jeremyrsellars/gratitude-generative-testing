(ns gratitude.storage)

(defprotocol IAppendNote
  (append! [this note]))

(defprotocol IRetrieveNotes
  (retrieve [this criteria]))

