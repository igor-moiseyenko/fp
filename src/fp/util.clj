(ns fp.util
  (:require [clojure.java.io :as io]
            [clojure.pprint :as pprint]))

"Read file line by line and return them as a vector."
(defn read-file
  [filePath]
  (with-open [reader (io/reader filePath)]
    (reduce conj [] (line-seq reader))))

"Pretty write-to-file function."
(defn write-dataset-to-file
  [outputPath dataset]
  (with-open [writer (io/writer outputPath)]
    (binding [*out* writer]
      (pprint/write dataset))))
