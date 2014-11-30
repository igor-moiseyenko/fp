(ns fp.lab1.core
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as str])
  (:require [fp.util :as util]))

"Lab 1 constants."
(def dataFilePath "./src/fp/lab1/glass.data.txt")
(def rA 2)
(def rB (* 1.5 rA))
(def alpha (/ 4 (* rA rA)))
(def beta (/ 4 (* rB rB)))
(def eps 0.55)
(def greatEps 0.5)
(def smallEps 0.15)

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

"Return squares of the distances for each of the specified data points as a map of maps.
 Allows not to recalculate square of the distance many times."
(defn- squares-of-the-distances
  [dataPoints]
  (reduce (fn [distanceSquares dataPointKey]
            (assoc distanceSquares dataPointKey (reduce (fn [dataPoint1DistanceSquares restDataPointKey]
                                                          (let [squareOfTheDistance (get-in distanceSquares [restDataPointKey dataPointKey])]
                                                            (assoc dataPoint1DistanceSquares
                                                                   restDataPointKey
                                                                   (if (not= squareOfTheDistance nil)
                                                                     squareOfTheDistance
                                                                     (square-of-the-distance (get dataPoints dataPointKey)
                                                                                             (get dataPoints restDataPointKey))))))
                                                        {}
                                                        (keys (dissoc dataPoints dataPointKey)))))
          {}
          (keys dataPoints)))

"Returns potential of the specified data point."
(defn- data-point-potential
  [dataPointId dataPoints distanceSquares]
  (let [restDataPoints (dissoc dataPoints dataPointId dataPoints)]
    (apply + (map (fn [restDataPointId]
                    (Math/pow Math/E (- (* alpha (get-in distanceSquares [dataPointId restDataPointId])))))
                  (keys restDataPoints)))))

"Returns potentials as a map of data point id & its potential for each of the specified data points."
(defn- data-points-potentials
  [dataPoints distanceSquares]
  (reduce (fn [dataPointsPotentials dataPointId]
            (assoc dataPointsPotentials dataPointId (data-point-potential dataPointId dataPoints distanceSquares)))
          {}
          (keys dataPoints)))

"Returns id of data point with mas potential."
(defn- get-data-point-id-with-max-potential
  [dataPointsPotentials]
  (key (apply max-key val dataPointsPotentials)))

"Revise potentials after one more cluster selected."
(defn- revise-potentials
  [clusterIds lastClusterPotentialId dataPointsPotentials distanceSquares]
  (reduce (fn [revisedPotentials restPotentialId]
            (assoc revisedPotentials
                   restPotentialId
                   (- (get dataPointsPotentials restPotentialId)
                      (* (get dataPointsPotentials lastClusterPotentialId)
                         (Math/pow Math/E (- (* beta (get-in distanceSquares [lastClusterPotentialId restPotentialId]))))))))
          {}
          (keys (apply dissoc dataPointsPotentials clusterIds))))

(defn- get-clusters
  [dataPoints dataPointsPotentials distanceSquares]
  (loop [lastClusterId (get-data-point-id-with-max-potential dataPointsPotentials)
         clusters { lastClusterId (get dataPointsPotentials lastClusterId) }
         revisedPotentials (revise-potentials (keys clusters) lastClusterId dataPointsPotentials distanceSquares)]
    (let [nextClusterId (get-data-point-id-with-max-potential revisedPotentials)]
      (prn nextClusterId)
      (prn (get revisedPotentials nextClusterId))
      (if (< (get revisedPotentials nextClusterId) (* eps (get dataPointsPotentials lastClusterId)))
        clusters
        (let [newClusters (assoc clusters nextClusterId (get revisedPotentials nextClusterId))
              newRevisedPotentials (revise-potentials (keys newClusters) nextClusterId dataPointsPotentials distanceSquares)]
          (recur nextClusterId newClusters newRevisedPotentials))))))

(defn run-cluster-estimation
  []
  (let [dataLines (util/read-file dataFilePath)
        dataPoints (getDataPoints dataLines)
        distanceSquares (squares-of-the-distances dataPoints)
        dataPointsPotentials (data-points-potentials dataPoints distanceSquares)
        clusters (get-clusters dataPoints dataPointsPotentials distanceSquares)]
    (prn dataPoints)
    (prn dataPointsPotentials)
    (prn clusters)))
