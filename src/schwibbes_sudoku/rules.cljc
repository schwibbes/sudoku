(ns schwibbes-sudoku.rules
  (:require [clojure.string :as s]))

(defn or-clause [literals]
  "render OR clause"
  (let [exp (s/join " v " literals) ]
    (str "(" exp  ")")))

(defn and-clause [literals] 
  "render AND clause"
  (let [exp (s/join "^" literals) ]
    (str "(" exp  ")")))

(defn maxone-clause [literals] 
  "render max one clause"
  (let [exp (s/join "," literals) ]
    (str "one-of(" exp  ")")))