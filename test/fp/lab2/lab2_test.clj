(ns fp.lab2.lab2-test
  (:require [clojure.test :refer :all]
            [fp.util :as util]
            [fp.lab2.core :as lab2Core]
            [fp.lab2.links :as links]))

(def testHtmlPath "./resources/lab2/lab2test.html")
(def numOfLinks 3)
(def link1 "http://www.hello1.com")
(def link2 "http://www.hello2.com")
(def link3 "www.hello3.com")

(deftest parse-links-test
  (testing "Parse links test"
      (let [links (links/parse-links (slurp testHtmlPath))]
        (is (and (not= links nil)
                 (= (count links) numOfLinks)))
        (is (= (nth links 0) link1))
        (is (= (nth links 1) link2))
        (is (= (nth links 2) link3)))))

(deftest parse-external-links-test
  (testing "Parse external links test"
    (let [externalLinks (links/filter-external-links (links/parse-links (slurp testHtmlPath)))]
      (is (and (not= externalLinks nil)
               (= (count externalLinks) 2)))
      (is (= (nth externalLinks 0) link1))
      (is (= (nth externalLinks 1) link2)))))
