(ns de.herrnorbert.EvolutionLoop.gui-repl.gui-repl
  "### REPL GUI
This package provides functions to evaluate a random _world_ and display the steps at the REPL.  
The code isn't really polished right now so don't be to shocked."
  (:use [de.herrnorbert.EvolutionLoop.core.utils :only [rnd-int-vec]])
  (:require [de.herrnorbert.EvolutionLoop.core.core :as core])
  (:import [de.herrnorbert.EvolutionLoop.core.core Animal World])  
  (:use [clojure.pprint]))

;; A _world_ with width 100, height 30 and a randomly added _animal_ with random genes.
(defn gen-world []
  (core/World. 
   [(core/Animal. [(rand-int 30) (rand-int 100)] 1000 0 (rnd-int-vec 8 10) {:age 0})]
   {}
   [[0 0 100 30] [45 10 10 10]]
   {:width  100
    :height 30
    :plant-energy 80
    :reproduction-energy 100}
   {:age 0}))

;; This atom holds the current world state and _will be mutated_!
(def world (atom (gen-world)))


;;
;; *Drawing functions*
;;

(defn draw-world
  "Displays the given world at the REPL.  
_Animals_ will be displayed by an `A`, _plants_ by a `*`."
  ([world]
     (draw-world world [-1 -1]))
  ([{:keys [plants animals stats] :as world} search]
      (let [width  (get-in world [:options :width])
            height (get-in world [:options :height])
            contains-animal? (fn [location]
                               (some #(= location (:location %)) animals))
            contains-plant? (fn [location]
                              (contains? plants location))
            contains-search? (fn [location]
                               (= location search))]
        (->> (for [y (range 0 height)
                   x (range 0 width)]
               (let [location [y x]]
                 (cond
                  (contains-search? location) "X"
                  (contains-animal? location) "A"
                  (contains-plant? location) "*"
                  :default " ")))
             (partition width)
             (map #(apply str %))
             (map println)
             (doall))
        (str "Year " (:age stats)))))

;;
;; *Game functions*
;;

(defn draw
  "Draws the current world at the REPL."
  ([]
     (draw-world @world))
  ([y x]
     (draw-world @world [y x])))

(defn restart
  "Creates a new world."
  []
  (reset! world (gen-world)))

(defn evolve
  "Evolves the world to its next state."
  []
  (reset! world (core/update-world @world)))

(defn evolve-times
  "Evolves the current world `count` times to its next state."
  [count]
  (let [rate (int (/ count 100))
        display-rate (if (zero? rate) 1 rate)
        should-display? #(zero? (mod % display-rate))]
    (print "0% ")
    (loop [i 1]
      (if (should-display? i)
        (print "."))
      (evolve)
      (if (>= i count)
        (do
          (println " 100%")
          (draw))
        (recur (inc i))))))

(defn evolve-and-draw-next-state
  "Evolves the world to its next state and displays it at the REPL."
  []
  (evolve)
  (draw))

(defn evolution-loop
  "Infinitely evolves and displays the world."
  []
  (loop [i 0]
    (draw)
    (evolve)
    (Thread/sleep 1000)
    (recur (inc i))))

;;
;; *Inspecting the world*
;;

(defn pprint-world
  "Prints the entire world struct to the REPL."
  []
  (pprint @world))

(defn pprint-animals
  "Prints the animals of the world to the REPL."
  []
  (pprint (:animals @world)))

(defn print-animal-positions
  "Prints the positions of all animals in the world to the REPL."
  []
  (map #(:location %) (:animals @world)))

(defn pprint-at-location [y x]
  "Prints the animal at the given location to the REPL."
  (let [animals (:animals @world)
        location [y x]]
    (pprint (filter #(= location (:location %)) animals))))
