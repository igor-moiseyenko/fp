(ns fp.lab1.core
  (:require [clojure.java.io :as io])
  (:require [fp.util :as util]))

(def dataFilePath "./src/fp/lab1/glass.data.txt")

(defn run-lab1
  []
  (prn (util/read-file dataFilePath)))
