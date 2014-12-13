(ns fp.lab2.lab2-test
  (:require [clojure.test :refer :all]
            [fp.util :as util]
            [fp.lab2.core :as lab2Core]
            [fp.lab2.urls :as urls]))

(def testHtmlPath "./resources/lab2/lab2test.html")
(def numOfUrls 3)
(def url1 "http://www.hello1.com")
(def url2 "http://www.hello2.com")
(def url3 "www.hello3.com")
(def url404 "http://www.wikipedia.org/x")

(deftest parse-urls-test
  (testing "Parse urls test."
      (let [urls (urls/parse-urls (slurp testHtmlPath))]
        (is (and (not= urls nil)
                 (= (count urls) numOfUrls)))
        (is (= (nth urls 0) url1))
        (is (= (nth urls 1) url2))
        (is (= (nth urls 2) url3)))))

(deftest parse-external-urls-test
  (testing "Parse external urls test."
    (let [externalUrls (urls/parse-external-urls (slurp testHtmlPath))]
      (is (and (not= externalUrls nil)
               (= (count externalUrls) 2)))
      (is (= (nth externalUrls 0) url1))
      (is (= (nth externalUrls 1) url2)))))

(deftest handle-url
  (testing "Handle url with 404 response status."
    (is (= (lab2Core/handle-url url404 1) "Bad"))))
