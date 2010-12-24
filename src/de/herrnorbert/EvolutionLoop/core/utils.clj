(ns de.herrnorbert.EvolutionLoop.core.utils
  "Some utils .. nothing special.")

(defn conj-in
  "Conjs the given values to the seq specified by key in base.
Return: 'updated base'"
  [base key & values]
  (let [oldvals (get base key)]
    (assoc base key (apply conj oldvals values))))

(defn rnd-int-vec
  "Generates a vector of random integers.
Return: 'count' element vector with rand ints up to 'max'"
  [count max]
  (vec
   (for [gene (range 0 count)]
     (rand-int max))))

