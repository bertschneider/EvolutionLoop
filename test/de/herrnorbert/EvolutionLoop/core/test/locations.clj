(ns de.herrnorbert.EvolutionLoop.core.test.locations
  (:use [de.herrnorbert.EvolutionLoop.core.locations] :reload)
  (:use [lazytest.describe :only (describe it testing)]))

(let [[top left width height :as location] [0 0 10 10]]

  (describe rnd-location "should create locations"
    (it "greater or equal to zero"
      (every? #(>= % 0) (rnd-location location)))
    (it "with y in top + height"
      (< (first (rnd-location location)) (+ top height)))
    (it "with x in left + width"
      (< (second (rnd-location location)) (+ left width)))))

(describe east (it "should inc x" (= [1 2] (east [1 1]))))
(describe west (it "should dec x" (= [1 0] (west [1 1]))))
(describe north (it "should dec y" (= [0 1] (north [1 1]))))
(describe south (it "should inc y" (= [2 1] (south [1 1]))))
(describe north-west (it "should dec y and x" (= [0 0] (north-west [1 1]))))
(describe north-east (it "should dec y and inc y" (= [0 2] (north-east [1 1]))))
(describe south-west (it "should inc y and dec x" (= [2 0] (south-west [1 1]))))
(describe south-east (it "should inc y and x" (= [2 2] (south-east [1 1]))))

(describe direction "The var direction"
  (it "should contain the direction functions in clockwise order"
    (= directions [north-west north north-east east south-east south south-west west])))
