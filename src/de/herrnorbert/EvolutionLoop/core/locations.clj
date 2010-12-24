(ns de.herrnorbert.EvolutionLoop.core.locations
  "Some functions to generate and/or manipulate locations in a 2d grid.
Position [0 0] is the top left corner of the word")

(defn rnd-location
  "Return a random location within the given boundaries."
  ([[left top width height]]
     (rnd-location left top width height))
  ([left top width height]
     (let [y (+ top  (rand-int height))
           x (+ left (rand-int width))]
       [y x])))

;;
;; Functions to move around in the world.
;;
(defn east  [[y x]] [y (inc x)])
(defn west  [[y x]] [y (dec x)])
(defn north [[y x]] [(dec y) x])
(defn south [[y x]] [(inc y) x])
(defn north-west [location] (-> (north location) (west)))
(defn north-east [location] (-> (north location) (east)))
(defn south-west [location] (-> (south location) (west)))
(defn south-east [location] (-> (south location) (east)))

;; A vector of all directions.
;; Position of the functions is:
;; 0 1 2
;; 7   3
;; 6 5 4
(def directions [north-west north north-east east south-east south south-west west])

