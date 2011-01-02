(ns de.herrnorbert.EvolutionLoop.core.test.utils
  (:use [de.herrnorbert.EvolutionLoop.core.utils] :reload)
  (:use [lazytest.describe :only (describe it testing)]))

(let [base {:key [1 2]}]
  
  (describe conj-in "should"
    (it "conj values onto existing ones"
      (= {:key [1 2 3 4]} (conj-in base :key [3 4])))
    (it "create the key if it is not present"
      (= {:key [1 2 3 4]} (conj-in {} :key [1 2 3 4])))))


(describe rnd-int-vec "should generates random vectors"
  (it "with given size and in given range"
    (let [v (rnd-int-vec 10 10)]
      (and (= 10 (count v))
           (every? #(< % 10) v)
           (every? #(>= % 0) v))))
  (it "without immediate repetition"
    (let [v1 (rnd-int-vec 10 10)
          v2 (rnd-int-vec 10 10)]
      (not (= v1 v2)))))
