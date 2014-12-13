(ns fp.lab2.core
  (:require [fp.util :as util]
            [fp.lab2.urls :as links])
  (:require [clj-http.client :as client]))

(declare run-url-analyzer)

(def htmlPath "./resources/lab2/lab2.html")

(defn- handle-url
  [url]
  )

(defn run-url-analyzer
  []
  (let [testHtml (slurp htmlPath)
        urls (links/parse-external-urls testHtml)]
    (prn 'Result)
    (prn urls)))
