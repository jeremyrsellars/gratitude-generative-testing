(ns presenter-aids.server
  (:require [clojure.core.async :as async]
            [clojure.java.io :as io]
            [clojure.pprint :as pprint]
            [clojure.string :as string]
            [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [clojure.tools.reader.edn :as edn]
            [clojure.tools.reader.reader-types :as rt]
            [clojure.test.check.generators]
            ring.util.request)
  (:import [java.io File FileInputStream]))

(defn- read-setting
  [k default]
  (or (System/getenv k) default))

(def history-file (read-setting "PRESENTER_HISTORY_FILE" "history.edn"))
(println "Recording history to" history-file)

(defn- load-edn
  [filename]
  (with-open [r (java.io.PushbackReader.
                 (clojure.java.io/reader filename))]
    (let [edn-seq (repeatedly (partial edn/read {:eof :theend} r))]
      (doall (take-while (partial not= :theend) edn-seq)))))


(defn default-handler
  [{:keys [uri request-method] :as request}]
  (println ::request request)
  {:status 404
   :headers {"Content-Type" "text/html"}
   :body (str "<h3>404 Not found: " (name request-method) " " uri "</h3><hr/>"
              "<code>" request "</code>")})

(defn two-digit-number
  [seconds]
  (let [s (int seconds)]
    (if (< s 10)
      (str 0 s)
      (str s))))

(defn format-time
  [seconds]
  (->> seconds
       int
       ((juxt #(two-digit-number (/ % 60))
              #(two-digit-number (rem % 60))))
       (string/join ":")))

(defn append-history-handler
  [{:keys [uri request-method body] :as request}]
  (println ::request request)
  (spit (io/as-file history-file)
    (str body)
    :append true)
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body "ACK"})

(defn history-report
  [events]
  (let [slides
        (->> (map (fn [[t1 u] [t2 _]][u (int (/ (- t2 t1) 1000.0))]) events (next events))
             (filter (comp pos? second)))
        elapsed (reductions + 0 (rest (map second slides)))
        slides (map conj slides elapsed)
        slides (map (fn [[u t e]][(format-time t) (format-time e) u]) slides)]
    (->> slides
         (map println)
         count
         println)))

(defn load-history-handler
  [{:keys [uri request-method] :as request}]
  (println ::request request)
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body (->> history-file
              load-edn
              history-report
              with-out-str)})

(defn static-handler [{:keys [uri request-method] :as request}]
  (let [handler (case [request-method uri]
                  [:post "/API/history"]         append-history-handler
                  [:get  "/API/history"]         load-history-handler
                  default-handler)]
    (handler request)))

(defn wrap-body-string [handler]
  (fn [request]
    (let [body-str (ring.util.request/body-string request)]
      (handler (assoc request :body body-str)))))

(def handler
  (-> static-handler
      wrap-body-string))
