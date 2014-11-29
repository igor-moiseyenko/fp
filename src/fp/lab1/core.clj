(ns fp.lab1.core
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as str])
  (:require [fp.util :as util]))

"Lab 1 constants."
(def dataFilePath "./src/fp/lab1/glass.data.txt")
(def rA 2)
(def rB (* 1.5 rA))
(def alpha (/ 4 (* rA rA)))

"Lab 1 interface."
(declare run-cluster-estimation)

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

"Returns square of the distance for specified data points."
(defn- square-of-the-distance
  [dataPoint1 dataPoint2]
  (let [attrs1 (get dataPoint1 1)
        attrs2 (get dataPoint2 1)]
    (apply + (map (fn [attr1 attr2]
                    (Math/abs (- attr2 attr1)))
                  attrs1
                  attrs2))))

"Returns potential of the specified data point."
(defn- data-point-potential
  [dataPoint dataPoints]
  (apply + (map (fn [dp]
                  (if (not= (get dataPoint 0) (get dp 0))
                    (Math/pow Math/E (- (* alpha (square-of-the-distance dataPoint dp))))
                    0))
                dataPoints)))

"Returns potentials as a vector of data point id & its potential elements for each of the specified data points."
(defn- data-points-potentials
  [dataPoints]
  (map (fn [dataPoint]
         [(get dataPoint 0) (data-point-potential dataPoint dataPoints)])
       dataPoints))

(defn run-cluster-estimation
  []
  (let [dataLines (util/read-file dataFilePath)
        dataPoints (getDataPoints dataLines)
        dataPointsPotentials (data-points-potentials dataPoints)]
    (prn dataPoints)
    (prn dataPointsPotentials)))
