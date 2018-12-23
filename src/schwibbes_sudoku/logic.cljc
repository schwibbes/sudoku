(ns schwibbes-sudoku.logic
  (:require
   [schwibbes-sudoku.rules :as r]
   [schwibbes-sudoku.util :as u]))


(defn- cell [[x y z]]
  "print vector of three literals"
  (str x y (u/letter z)))


(defn fill-cells [n] 
  "create constraints to fill each cell"
  (let [n2 (* n n)
        rng (range 1 (inc n2))]
      (r/and-clause 
        (flatten 
          (for [x rng]
            (for [y rng]
              (r/or-clause 
                (for [z rng] (cell [x y z])))))))))
     
(defn unique-rows [n] 
  "create constraints to have unique cells in each row"
  (let [n2 (* n n)
        rng (range 1 (inc n2))] 
      (r/and-clause 
        (for [x rng z rng]
          (r/maxone-clause (for [y rng] (cell [x y z])))))))
     
    
(defn unique-cols [n] 
  "create constraints to have unique cells in each column"
  (let [n2 (* n n)
        rng (range 1 (inc n2))]
      (r/and-clause 
        (for [y rng z rng]
          (r/maxone-clause (for [x rng] (cell [x y z])))))))
     

(defn unique-box [n] 
  "create constraints to have unique cells in each box"
  (let [n2 (* n n)
        rng (range 1 (inc n2))
        blocks (for [x (partition n rng)] (for [y (partition n rng)] [x y]))]
    (r/and-clause
      (flatten 
        (for [block blocks]
          (for [rows block] 
            (for [symbol rng] 
              (r/maxone-clause 
                (map cell
                  (map #(reverse (conj % symbol)) (u/cross (vec rows))))))))))))


(defn index2xy [n i]
  """returns (x,y) for continouous index in rectangular array with size n^2 x n^2.
  indices start at 1.
  """
  (let [n2 (* n n)
        row (mod i n2)
        col (int (Math/floor (/ i n2)))]
    (map inc [row col])))


(defn parse-sudoku [n ascii]
  """convert a string of numbers in the from
    '324.143223.4412.'
    to a list of filled literals (11C, 12B)
    dots in the input denote unfilled cells
    and digits 1-9 denote filled cells.
    n^2 represents the number of rows/cols
  """
{:pre [(= (* n n n n) (count ascii))]}
  (let [with-idx (map-indexed vector ascii)]
    (for [zipped with-idx
        :when (not= (second zipped) \.)
        :let [coord (index2xy n (first zipped))
              symbol (u/to-int (str (second zipped)))]] 
        (cell (concat coord [symbol])))))