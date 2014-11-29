(ns fp.lab1.core
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as str])
  (:require [fp.util :as util]))

(def dataFilePath "./src/fp/lab1/glass.data.txt")

"Get data point with its id and attributes as a vector from the specified lines of data.
 String values will be cast to its number equivalents."
(defn- getDataPoints
  [dataLines]
  (map (fn [dataLine]
         (let [dataPointVector (str/split dataLine #",")
               dataPointId (read-string (get dataPointVector 0))
               dataPointAttributes (vec (map read-string (subvec dataPointVector 1)))]
           [dataPointId dataPointAttributes]))
       dataLines))

(defn run-lab1
  []
  (let [dataLines (util/read-file dataFilePath)]
    (prn (getDataPoints dataLines))))
