(ns gratitude.doc.core
  (:require [clojure.java.io :as io]))

(defmacro readme
  []
  (let [f (io/as-file "README.md")]
    (when-let [s (and (.exists f) (slurp f))]
      s)))

(defmacro todo
  []
  (let [f (io/as-file "TODO.md")]
    (when-let [s (and (.exists f) (slurp f))]
      s)))

(defmacro softwarecraftsmanship
  []
  (let [f (io/as-file "manifesto.softwarecraftsmanship.org.md")]
    (when-let [s (and (.exists f) (slurp f))]
      s)))

(defmacro slide-markdown
  [filename]
  (let [f (and (re-matches #"(?i)[-_.a-z0-9]+" filename) (io/as-file (str "resources/public/slides/" filename)))]
    (if-let [s (and f (.exists f) (slurp f))]
      s
      (str "# Slide not found: " filename "\r\n" f))))
