(ns de.herrnorbert.EvolutionLoop.test.core
  (:use [de.herrnorbert.EvolutionLoop.core] :reload)
  (:use [midje.sweet]))

;.;. FAIL for (NO_SOURCE_FILE:1)
;.;. You claimed the following was needed, but it was never used:
;.;. (rnd-location [0 0 10 10])
;.;. 
;.;. FAIL at (NO_SOURCE_FILE:1)
;.;. Expected: #{[2 2] [1 1]}
;.;.   Actual: {"this Throwable was captured by midje:" #<StackOverflowError java.lang.StackOverflowError>}
(let [[top left] [0 0]
      [width height] [10 10]
      in-min-range? (fn [[x y]] (and (< x width) (>= x left) (< y height) (>= y top)))
      in-max-range? (fn [[x y]] (and (< x width) (< y height)))]

  (facts "Some random locations in the given range"
         (rnd-location [0 0 0 0]) => (fn [[x y]] (= 0 x y))
         (rnd-location [0 0 10 10]) => in-min-range?
         (rnd-location [10 10 0 0]) => in-max-range?)

  (facts "Add some random locations to given ones"
         (add-rnd-location-to #{} [0 0 0 0]) => #{[0 0]}
         (add-rnd-location-to #{} [0 0 10 10]) => #{[5 5]}
         (provided
           (rnd-location [0 0 10 10]) => [5 5])
         (add-rnd-location-to #{} [0 0 10 10]) => #(every? in-min-range? %)
         (add-rnd-location-to #{} [10 10 0 0]) => #(every? in-max-range? %)
         (add-rnd-location-to #{[1 1]} [0 0 10 10]) => #{[5 5] [1 1]}
         (provided
           (rnd-location [0 0 10 10]) => [5 5])
         (add-rnd-location-to #{[1 1]} [0 0 10 10]) => #{[1 1] [2 2]}
         (provided
           (rnd-location [0 0 10 10]) => [1 1]
           (rnd-location [0 0 10 10]) => [2 2])))

(let [world  [0 0 100 100]
      jungle [45 45 10 10]]
  (fact "Add nothing to the world"
    (add-plants #{}) => #{})
  (fact "Add a plant to the world"
    (add-plants #{} world) => #{[50 50]}
    (provided
      (rnd-location world) => [50 50]))
  (fact "Add plants in different locations"
    (add-plants #{} world jungle) => #{[10 10] [50 50]}
    (provided
      (rnd-location world) => [10 10]
      (rnd-location jungle) => [50 50]))
  (fact "Add a plant in a populated world"
    (add-plants #{[50 50]} world) => #{[50 50] [10 10]}
    (provided
      (rnd-location world) => [10 10])))

  

