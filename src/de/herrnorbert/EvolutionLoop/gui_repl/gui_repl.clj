(ns de.herrnorbert.EvolutionLoop.gui-repl.gui-repl
  "### REPL GUI
This package provides functions to evaluate a random _world_ and display the steps at the REPL.  
The code isn't really polished right now so don't be to shocked."
  (:use [de.herrnorbert.EvolutionLoop.core.utils :only [rnd-int-vec]])
  (:use [de.herrnorbert.EvolutionLoop.core.core :only [update-world]])
  (:use [clojure.pprint]))


;; A _world_ with width 100, height 30 and a randomly added _animal_ with random genes.
(defn gen-world []
  {:animals
   [{:location [(rand-int 30) (rand-int 100)]
     :energy 1000
     :direction 0
     :genes (rnd-int-vec 8 10)}]
   :plants {}
   :areas [[0 0 100 30] [45 10 10 10]]
   :options {:width  100
             :height 30
             :plant-energy 80
             :reproduction-energy 300}})

;; This atom holds the current world state and _will be mutated_!
(def world (atom (gen-world)))


;; *Some drawing functions*

(defn has-location?
  "Checks if something in `values` contains the given location at key `:location`."
  [location values]
  (some #(= location (:location %)) values))

(defn draw-world
  "Displays the given world at the REPL.  
_Animals_ will be displayed by an `A`, _plants_ by a `*`."
  [{:keys [plants animals] :as world}]
  (let [width  (get-in world [:options :width])
        height (get-in world [:options :height])]
    (->> (for [y (range 0 height)
               x (range 0 width)]
           (cond
            (has-location? [y x] animals) "A"
            (contains? plants [y x]) "*"
            :default " "))
         (partition width)
         (map #(apply str %))
         (map println)
         (doall))
    ""))


;; *Game functions*

(defn draw
  "Draws the current world at the REPL."
  []
  (draw-world @world))

(defn restart
  "Creates a new world."
  []
  (reset! world (gen-world)))

(defn evaluate
  "Evaluates the world to its next state."
  []
  (reset! world (update-world @world)))

(defn evaluate-times
  "Evaluates the current world `count` times to its next state."
  [count]
  (let [display-rate (int (/ count 100))
        should-display? #(zero? (mod % display-rate))]
    (loop [i 1]
      (if (should-display? i)
        (println "Evaluating step " i))
      (evaluate)
      (if (>= i count)
        (draw)
        (recur (inc i))))))

(defn evaluate-and-draw-next-state
  "Evaluates the world to its next state and displays it at the REPL."
  []
  (evaluate)
  (draw))

(defn evaluation-loop
  "Infinitly evaluates and displays the world."
  []
  (loop [i 0]
    (println "Year " i)
    (draw)
    (evaluate)
    (Thread/sleep 1000)
    (recur (inc i))))

(defn pprint-world
  "Prints the world struct to the REPL."
  []
  (pprint @world))

(defn pprint-animals
  "Prints the animals of the world."
  []
  (pprint (:animals @world)))

(defn pprint-at-location [y x]
  "Prints the animal at the given location."
  (let [animals (:animals @world)
        location [y x]]
    (pprint
     (doall  (filter #(= location (:location %)) animals)))))
