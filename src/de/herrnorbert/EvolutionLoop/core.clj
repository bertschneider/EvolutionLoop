(ns de.herrnorbert.EvolutionLoop.core
  (:use [clojure.set]))

(defn rnd-location
  "Return a random location within the given boundaries"
  [[left top width height]]
  (let [x (rand-int (+ left (rand-int width)))
        y (rand-int (+ top (rand-int height)))]
    [x y]))

(defn add-rnd-location-to
  "Returns the given set with a randomly added location within the stated boundaries."
  [locations area]
  (let [location (rnd-location area)]
    (if (contains? locations location)
      (add-rnd-location-to locations area)
      (conj locations location))))

(defn add-plants [plants & areas]
  (union plants (apply union (map #(add-rnd-location-to plants %) areas))))
