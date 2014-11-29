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

"Get data points with its id and attributes as a map from the specified lines of data.
 String values will be cast to its number equivalents."
(defn- getDataPoints
  [dataLines]
  (reduce (fn [dataPoints dataLine]
            (let [dataPointVector (str/split dataLine #",")
                  dataPointId (read-string (get dataPointVector 0))
                  dataPointAttributes (vec (map read-string (subvec dataPointVector 1)))]
              (assoc dataPoints dataPointId dataPointAttributes)))
          {}
          dataLines))

"Returns square of the distance for specified data points attributes."
(defn- square-of-the-distance
  [attrs1 attrs2]
  (apply + (map (fn [attr1 attr2]
                  (Math/abs (- attr2 attr1)))
                attrs1
                attrs2)))

"Returns potential of the specified data point."
(defn- data-point-potential
  [dataPointId dataPoints]
  (let [dataPoint (get dataPoints dataPointId)
        restDataPoints (dissoc dataPoints dataPointId dataPoints)]
    (apply + (map (fn [restDataPointId]
                    (Math/pow Math/E (- (* alpha (square-of-the-distance dataPoint (get restDataPoints restDataPointId))))))
                  (keys restDataPoints)))))

"Returns potentials as a map of data point id & its potential for each of the specified data points."
(defn- data-points-potentials
  [dataPoints]
  (reduce (fn [dataPointsPotentials dataPointId]
            (assoc dataPointsPotentials dataPointId (data-point-potential dataPointId dataPoints)))
          {}
          (keys dataPoints)))

(defn run-cluster-estimation
  []
  (let [dataLines (util/read-file dataFilePath)
        dataPoints (getDataPoints dataLines)
        dataPointsPotentials (data-points-potentials dataPoints)]
    (prn dataPoints)
    (prn dataPointsPotentials)))
