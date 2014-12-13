(ns fp.lab2.core
  (:require [fp.util :as util]
            [fp.lab2.urls :as urls])
  (:require [clj-http.client :as client]))

(declare run-url-analyzer)
(declare handle-urls)
(declare handle-url)

"Status codes to handle."
(def ok 200)
(def notFound 404)
(def redirects [300 301 302 303 307])

"Initial parameters."
(def linksPath "./resources/lab2/links")
(def depth 2)

"Path to the output."
(def outputPath "./resources/lab2/output")

(defn handle-url
  "Handle external url.
  Depth-first algorithm for traversing URL's graph.
  Recall function to handle urls with decrementet depth."
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

(defn handle-urls
  "Handle external urls with specified depth."
  [externalUrls depth]
  (pmap (fn [externalUrl]
          { externalUrl (handle-url externalUrl depth) })
        externalUrls))

(defn run-url-analyzer
  "Run urls analyzer."
  []
  (let [urls (util/read-file linksPath)
        urlsInfo (handle-urls urls depth)]
    (prn urls)
    (prn 'Processing...)
    (prn urlsInfo)
    (util/write-dataset-to-file outputPath urlsInfo)))
