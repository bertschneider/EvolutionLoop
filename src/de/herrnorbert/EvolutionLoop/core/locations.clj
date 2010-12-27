(ns de.herrnorbert.EvolutionLoop.core.locations
  "### Some functions to generate and/or manipulate locations in a 2D grid.
Position `[0 0]` is the top left corner of the word." )

(defn rnd-location
  "Returns a random location within the given boundaries."
  ([^ints loc]
     (rnd-location (loc 0) (loc 1) (loc 2) (loc 3)))
  ([left top width height]
     (let [y (+ top  (rand-int height))
           x (+ left (rand-int width))]
       [y x])))

;; Functions to move around in the world.

(defn east [^ints [y x]] [y (inc x)])
(defn west [^ints [y x]] [y (dec x)])
(defn north [^ints [y x]] [(dec y) x])
(defn south [^ints [y x]] [(inc y) x])
(defn north-west [^ints location] (-> (north location) (west)))
(defn north-east [^ints location] (-> (north location) (east)))
(defn south-west [^ints location] (-> (south location) (west)))
(defn south-east [^ints location] (-> (south location) (east)))

(def ^{:doc "A vector of all direction functions."}
     directions [north-west north north-east east south-east south south-west west])

