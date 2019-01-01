(ns gratitude.doc.core
  (:require [clojure.string :as string]
            [clojure.java.io :as io]
            [devcards.core :refer [defcard deftest]]))

(defn symbol-hash
 ([ns-str sym]
  (str "#!/" (string/replace (str ns-str) "-" "_") "/" sym))
 ([sym]
  (str "#!/" (string/replace (str sym) "-" "_"))))

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
      (str "# Slide set not found: " filename "\r\n" f))))

(defmacro slide-markdown-cards
  [filename]
  (let [f (and (re-matches #"(?i)[-_.a-z0-9]+" filename) (io/as-file (str "resources/public/slides/" filename)))
        prefix (str "#!/" (string/replace (str *ns*) "-" "_") "/")
        slide-set-markdown
        (or (and f (.exists f) (slurp f))
            (str "# Slide set not found: " filename "\r\n" f))
        slide-split-regex #"(?i)(?m)^------*"
        slide-extras-regex #"(?i)^(?:\#([-a-z0-9_]+))?(?:\.([-.a-z0-9_]+))?\r?\n"
        slides
        (into []
          (for [[idx extra+markdown] (map-indexed vector (clojure.string/split slide-set-markdown slide-split-regex))
                  :let [[_ id classname] (re-find slide-extras-regex extra+markdown)
                        markdown (string/replace extra+markdown slide-extras-regex "")
                        first-line (re-find #"(?i)\w[-_ a-z0-9'\"]+" markdown)
                        card-sym (symbol (str *ns*) (str (or id (str "slide-" idx))))]]
            {:card-sym card-sym
             :classname classname
             :markdown markdown,
             :blank? (string/blank? markdown)
             :description first-line
             :hash (symbol-hash (namespace card-sym) (name card-sym))}))]
    (list 'do
      (cons 'clojure.core/vector
        (for [{:keys [card-sym classname markdown blank?]} slides
              :when (not blank?)]
          `(devcards.core/defcard ~card-sym
            (sab/html
              [:div {:dangerouslySetInnerHTML {"__html" (devcards.util.markdown/markdown-to-html ~markdown)}}])
            {:object {:render (us.sellars.slides.higlight-js/schedule-code-highlighting)}}
            {:classname ~classname})))
      (mapv
        #(select-keys % [:hash :classname :markdown :description :blank?])
        slides))))
