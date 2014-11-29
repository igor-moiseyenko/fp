(ns fp.util
  (:require [clojure.java.io :as io]))

"Read file line by line and return them as a vector."
(defn read-file
  [filePath]
  (with-open [reader (io/reader filePath)]
    (reduce conj [] (line-seq reader))))
