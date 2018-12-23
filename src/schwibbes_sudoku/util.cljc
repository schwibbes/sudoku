(ns schwibbes-sudoku.util
  (:require [clojure.string :as s]))

(defn to-int [s]
	#?(:clj  (java.lang.Integer/parseInt s)
       :cljs (js/parseInt s)))

(defn letter [num] 
  "convert number to letter starting from A.."
  (char (+ 64 num)))

;; lib
(defn cross [colls]
  (if (empty? colls)
    '(())
    (for [x (first colls)
          more (cross (rest colls))]
      (cons x more))))