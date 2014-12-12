(ns fp.lab2.links)

(declare parse-links)
(declare filter-external-links)

(def aHrefPattern (re-pattern "<a\\s+(?:[^>]*?\\s+)?href=\"([^\"]*)\""))
(def httpPrefix "http://")
(def httpsPrefix "https://")

(defn parse-links
  "Parse html content to find links."
  [html]
  (map (fn [match]
         (get match 1))
       (re-seq aHrefPattern html)))

(defn- external-link-filter
  "Check on presence of external URL prefix."
  [link]
  (if (or (.startsWith link httpPrefix)
          (.startsWith link httpsPrefix))
    true
    false))

(defn filter-external-links
  "Filter links. Returns only external."
  [links]
  (filter external-link-filter links))
