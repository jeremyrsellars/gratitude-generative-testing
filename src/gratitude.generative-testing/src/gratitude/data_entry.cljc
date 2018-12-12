(ns gratitude.data-entry)

(defprotocol INoteService
  (invalid-explanation [this note])
  (send! [this note]))
