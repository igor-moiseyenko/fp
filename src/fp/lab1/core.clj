(ns fp.lab1.core
  (:require [clojure.string :as str])
  (:require [fp.util :as util]))

"Lab 1 constants."
(def dataFilePath "./src/fp/lab1/glass.data.txt")
(def rA 2)
(def rB (* 1.5 rA))
(def alpha (/ 4 (* rA rA)))
(def beta (/ 4 (* rB rB)))
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
  (let [restDataPoints (dissoc dataPoints dataPointId)]
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
  [lastClusterCenterId lastClusterCenterPotential restDataPointsPotentials distanceSquares]
  (reduce (fn [revisedPotentials restPotentialId]
            (assoc revisedPotentials
                   restPotentialId
                   (- (get restDataPointsPotentials restPotentialId)
                      (* lastClusterCenterPotential
                         (Math/pow Math/E (- (* beta (get-in distanceSquares [lastClusterCenterId restPotentialId]))))))))
          {}
          (keys restDataPointsPotentials)))

"Returns shortest of the distances between potential cluster and all previously found cluster centers."
(defn- shortest-distance-to-cluster-center
  [nextClusterCenterId clusterCenters distanceSquares]
  (apply min (map (fn [clusterCenterId]
                    (get-in distanceSquares [nextClusterCenterId clusterCenterId]))
                  (keys clusterCenters))))

"Get cluster centers for specified data points based on specified constant values (rA, rB, greatEps, smallEps)."
(defn- get-cluster-centers
  [dataPointsPotentials distanceSquares]
  (loop [lastClusterCenterId (get-data-point-id-with-max-potential dataPointsPotentials)
         clusterCenters { lastClusterCenterId (get dataPointsPotentials lastClusterCenterId) }
         revisedPotentials (revise-potentials lastClusterCenterId
                                              (get dataPointsPotentials lastClusterCenterId)
                                              (dissoc dataPointsPotentials lastClusterCenterId)
                                              distanceSquares)]
    (let [nextClusterCenterId (get-data-point-id-with-max-potential revisedPotentials)
          nextClusterCenterPotential (get revisedPotentials nextClusterCenterId)
          lastClusterCenterPotential (get dataPointsPotentials lastClusterCenterId)]
      (if (< nextClusterCenterPotential (* smallEps lastClusterCenterPotential))
        clusterCenters
        (let [dMin (shortest-distance-to-cluster-center nextClusterCenterId clusterCenters distanceSquares)
              isNextClusterCenter (or (> nextClusterCenterPotential (* greatEps lastClusterCenterPotential))
                                      (>= (+ (/ dMin rA) (/ nextClusterCenterPotential lastClusterCenterPotential)) 1))
              clusterCenters (if isNextClusterCenter
                               (assoc clusterCenters nextClusterCenterId (get revisedPotentials nextClusterCenterId))
                               clusterCenters)
              revisedPotentials (if isNextClusterCenter
                                  revisedPotentials
                                  (assoc revisedPotentials nextClusterCenterId 0))
              lastClusterCenterId (if isNextClusterCenter nextClusterCenterId lastClusterCenterId)
              revisedPotentials (revise-potentials lastClusterCenterId
                                                   (get dataPointsPotentials lastClusterCenterId)
                                                   (dissoc revisedPotentials lastClusterCenterId)
                                                   distanceSquares)]
          (recur lastClusterCenterId clusterCenters revisedPotentials))))))

(defn run-cluster-estimation
  []
  (let [dataLines (util/read-file dataFilePath)
        dataPoints (getDataPoints dataLines)
        distanceSquares (squares-of-the-distances dataPoints)
        dataPointsPotentials (data-points-potentials dataPoints distanceSquares)
        clusterCenters (get-cluster-centers dataPointsPotentials distanceSquares)]
    (println "FP. Lab 1 - Cluster estimation.")
    (println "Cluster centers: " (keys clusterCenters))))
