(ns fp.lab2.core
  (:require [fp.util :as util]
            [fp.lab2.links :as links]))

(declare run-url-analyzer)

(def htmlPath "./resources/lab2/lab2test.html")

(defn run-url-analyzer
  []
  (let [testHtml (slurp "http://www.codecademy.com/")
        links (links/filter-external-links (links/parse-links testHtml))]
    (prn 'Result)
    (prn links)))
