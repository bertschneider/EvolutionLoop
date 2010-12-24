(ns de.herrnorbert.EvolutionLoop.test.core
  (:use [de.herrnorbert.EvolutionLoop.core] :reload)
  (:use [midje.sweet]))

;;
;; Locations
;;

(let [in-min-range? (fn [[y x]] (and (< x @width) (>= x 0) (< y @height) (>= y 0)))
      in-max-range? (fn [[y x]] (and (< x @width) (< y @height)))]
  (facts "Some random locations in the given range"
    (rnd-location [0 0 0 0])   => (fn [[y x]] (= 0 x y))
    (rnd-location [0 0 10 10]) => in-min-range?
    (rnd-location [10 10 0 0]) => in-max-range?))

(fact "Going east should inc the x coordinate"
  (east [1 1]) => [1 2])
(fact "Going west should dec the x coordinate"
  (west [1 1]) => [1 0])
(fact "Going south should inc the y coordinate"
  (south [1 1]) => [2 1])
(fact "Going north should dec the y coordinate"
  (north [1 1]) => [0 1])
(fact "Going north-west should dec the x and y coordinate"
  (north-west [1 1]) => [0 0])
(fact "Going north-east should dec the y and inc the x coordinate"
  (north-east [1 1]) => [0 2])
(fact "Going south-west should inc the y and dec the x coordinate"
  (south-west [1 1]) => [2 0])
(fact "Going south-east should inc the y and x coordinate"
  (south-east [1 1]) => [2 2])
(facts "The world should wrap at the corners"
  (north-west [0 0]) => [(dec @height) (dec @width)]
  (north-east [0 (dec @width)]) => [(dec @height) 0]
  (south-east [(dec @height) (dec @width)]) => [0 0]
  (south-west [(dec @height) 0]) => [0 (dec @width)])

;;
;; Plants
;;
(let [world @world
      jungle @jungle]

  (fact "Add nothing to the world"
    (add-plants #{}) => #{})
  
  (fact "Add a plant to the world"
    (add-plants #{} world) => #{{:location [50 50] :energy @plant-energy}}
    (provided
      (rnd-location world) => [50 50]))

  (fact "Add plants in different locations"
    (add-plants #{} world jungle)
    => #{{:location [10 10] :energy @plant-energy}
         {:location [50 50] :energy @plant-energy}}
    (provided
      (rnd-location world) => [10 10]
      (rnd-location jungle) => [50 50]))

  (fact "Add a plant in a populated world"
    (add-plants #{{:location [50 50] :energy @plant-energy}} world)
    => #{{:location [50 50] :energy @plant-energy}
         {:location [10 10] :energy @plant-energy}}
    (provided
      (rnd-location world) => [10 10])))
 
;;
;; Animals
;;

(fact "Create random genes"
  (rnd-genes) => (fn [genes] (every? #(and (< % @max-gene) (>= % 0)) genes)))

(fact "Animals should be moved in their direction"
  (move {:location [5 5] :direction 4 :energy 10})
  =>    {:location [6 6] :direction 4 :energy 9})

(fact "Animals should turn to a new direction"
  (turn {:direction 0 :genes [0 0 10 0 0 0 0 0 0]})
  =>    {:direction 2 :genes [0 0 10 0 0 0 0 0 0]})

(fact "Eating should increse the energy of an animal"
  (eat #{{:location [5 5] :energy 10}} {:location [5 5] :energy 10})
  => {:location [5 5] :energy 20})

(fact "Reproduce should create an additional animal"
  (let [reproduced? (fn [[parent child]]
                      (and (= (:location parent) (:location child))
                           (= (:energy parent) (:energy child))))]
    (reproduce {:location [1 1] :energy 1000 :genes [0 0 0 0 0 0 0 0]})
    => reproduced?))

