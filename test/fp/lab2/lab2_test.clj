(ns fp.lab2.lab2-test
  (:require [clojure.test :refer :all]
            [fp.util :as util]
            [fp.lab2.core :refer :all]))

(def testHtmlPath "./resources/lab2/lab2test.html")
(def numOfLinks 3)
(def link1 "http://www.hello1.com")
(def link2 "http://www.hello2.com")
(def link3 "http://www.hello3.com")

(deftest parse-links-test
  (testing "Parse links test"
      (let [links (parse-links (slurp htmlPath))]
      (is (and (not= links nil)
               (= (count links) numOfLinks)))
      (is (= (nth links 0) link1))
      (is (= (nth links 1) link2))
      (is (= (nth links 2) link3)))))
