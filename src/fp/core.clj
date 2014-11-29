(ns fp.core
  (:require [fp.lab1.core :as lab1]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!")
  (lab1/run-cluster-estimation))
