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
