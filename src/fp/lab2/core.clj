(ns fp.lab2.core
  (:require [fp.util :as util]))

(declare run-url-analyzer)

(def htmlPath "./resources/lab2/lab2test.html")
(def aHrefPattern (re-pattern "<a\\s+(?:[^>]*?\\s+)?href=\"([^\"]*)\""))

(defn parse-links
  [html]
  (map (fn [match]
         (get match 1))
       (re-seq aHrefPattern html)))

(defn run-url-analyzer
  []
  (let [testHtml (slurp htmlPath)
        links (parse-links testHtml)]
    (prn testHtml)
    (prn 'Result)
    (prn links)))
