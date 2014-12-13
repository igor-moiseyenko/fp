(ns fp.lab2.urls)

(declare parse-urls)
(declare filter-external-urls)
(declare parse-external-urls)

(def aHrefPattern (re-pattern "<a\\s+(?:[^>]*?\\s+)?href=\"([^\"]*)\""))
(def httpPrefix "http://")
(def httpsPrefix "https://")

(defn parse-urls
  "Parse html content to find urls."
  [html]
  (map (fn [match]
         (get match 1))
       (re-seq aHrefPattern html)))

(defn- external-url-filter
  "Check on presence of external URL prefix."
  [url]
  (if (or (.startsWith url httpPrefix)
          (.startsWith url httpsPrefix))
    true
    false))

(defn filter-external-urls
  "Filter URLs. Returns only external."
  [urls]
  (filter external-url-filter urls))

(defn parse-external-urls
  [html]
  (filter-external-urls (parse-urls html)))
