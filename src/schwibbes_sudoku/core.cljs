(ns ^:figwheel-hooks schwibbes-sudoku.core
  (:require
   [goog.dom :as gdom]
   [reagent.core :as reagent :refer [atom]]
   [schwibbes-sudoku.logic :as l]
   [schwibbes-sudoku.util :as u]))

(enable-console-print!)

(defonce app-state 
  (atom {:grid-size 2 
         :sudoku "8..6..9.5.............2.31...7318.6.24.....73...........279.1..5...8..36..3......"}))

#_(add-watch app-state :watcher
  (fn [key atom old-state new-state]
    (prn "-- Atom Changed --")
    (prn "key" key)
    (prn "atom" atom)
    (prn "old-state" old-state)
    (prn "new-state" new-state)))

(defn constraints [n]
  {:fill-cells (l/fill-cells n)
  :unique-rows (l/unique-rows n)
  :unique-cols (l/unique-cols n)
  :unique-box (l/unique-box n)})

(defn size-selector []
  [:p
    [:span "block-size: "]
    [:input {:type "number"
             :style {:width "3em"}
             :value (:grid-size @app-state)
             :on-change #(swap! app-state assoc :grid-size (u/to-int (-> % .-target .-value)))}]])

(defn sudoku-input []
  [:p 
    [:span "input: "] 
    [:input {:type "text"
             :style {:width "95%"}
             :value (:sudoku @app-state)
             :on-change #(swap! app-state assoc :sudoku (-> % .-target .-value))}]])

(defn render-constraint [[k v] nodes]
  [:p 
    [:strong (print-str (name k))]
    [:span (print-str v)]])

(defn render-encoded-sudoku []
  [:p (print-str (l/parse-sudoku (:grid-size @app-state) (:sudoku @app-state)))])


(defn entrypoint []
  (let [cnstr (constraints (:grid-size @app-state))]
    [:div
      [:h1 "solving sudoku using SAT solvers"]
      [:div.alert.alert-info
        [:strong "HOW TO: encode a sudoku instance as boolean literals"]
        (sudoku-input)
        (render-encoded-sudoku)]
      [:div.alert.alert-info
        [:strong "HOW TO: express game rules as boolean clauses"]
        [:div.panel-body
          (size-selector)
          (map render-constraint cnstr)]]]))


(defn get-app-element []
  (gdom/getElement "app"))

(defn mount [el]
  (reagent/render-component [entrypoint] el))

(defn mount-app-element []
  (when-let [el (get-app-element)]
    (mount el)))

(mount-app-element)

(defn ^:after-load on-reload []
  (mount-app-element))
