(ns fp.lab2.core
  (:require [fp.util :as util]
            [fp.lab2.urls :as urls])
  (:require [clj-http.client :as client]))

(declare run-url-analyzer)
(declare handle-urls)

"Status codes to handle."
(def ok 200)
(def notFound 404)
(def redirects [300 301 302 303 307])

"Initial parameters."
(def linksPath "./resources/lab2/links")
(def depth 2)

"Path to the output."
(def outputPath "./resources/lab2/output")

"Handle external url.
 Depth-first algorithm for traversing URL's graph.
 Recall function to handle urls with decrementet depth."
(defn- handle-url
  [externalUrl depth]
  (let [response (client/get externalUrl { :throw-exceptions false })
        statusCode (:status response)]
    (cond
      (= statusCode ok) (let [externalSubUrls (urls/parse-external-urls (:body response))]
                          { :numOfLinks (count externalSubUrls)
                            :links (if (> depth 0)
                                     (handle-urls externalSubUrls (- depth 1))
                                     []) })
      (= statusCode notFound) "Bad"
      (contains? redirects statusCode) (let [traceRedirects (:trace-redirects response)]
                                         (str "Redirect to " (peek traceRedirects))))))

"Handle external urls with specified depth."
(defn- handle-urls
  [externalUrls depth]
  (pmap (fn [externalUrl]
          { externalUrl (handle-url externalUrl depth) })
        externalUrls))

"Run urls analyzer."
(defn run-url-analyzer
  []
  (let [urls (util/read-file linksPath)
        urlsInfo (handle-urls urls depth)]
    (prn 'Result)
    (prn urls)
    (prn urlsInfo)
    (util/write-dataset-to-file outputPath urlsInfo)))
