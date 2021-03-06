(ns de.herrnorbert.EvolutionLoop.core.core
  "### The core namespace of this little game.

The main animal functions return a vector like `[updated-world animal]`. This vector is shifted from one function, like `eat`, to the other functions, like `reproduce`.  
Evaluate the world to the next state with `update-world`."
  (:use [de.herrnorbert.EvolutionLoop.core.locations :only [rnd-location directions]])
  (:use [de.herrnorbert.EvolutionLoop.core.utils :only [rnd-int-vec conj-in]]))

;; *Some constants*  
;; They can be overwritten in the world struct.
(def *default-height* 100)
(def *default-width* 100)
(def *default-plant-energy* 80)
(def *default-reproduction-energy* 300)

;; A _plant_ only consists of a location and some energy an animal can gain by eating it.
(defrecord Plant [^ints location ^int energy])

;; An _animal_ consists of a location, energy (remaining turns it can survive
;; without eating), a direction and some genes which define the
;; direction in the next turn.  
;; Directions state the next cell the animal will go to:
;;
;;     0 1 2  
;;     7 A 3  
;;     6 5 4
;;
(defrecord Animal [^ints location ^int energy ^int direction ^ints genes stats])

;; The _world_ consists of the animals, plants, different areas and
;; some options and stats. 
;;
;;     Options: :width
;;              :height
;;              :plant-energy
;;              :reproduction-energy
;;
;;     Stats: :age
;;         
(defrecord World [animals plants areas options stats])


;; *Plants*

(defn add-plants
  "Adds a randomly located _plant_ in every given area and returns the updated _world_.  
Return: `[world]`"
  [{:keys [plants areas] :as world}]
  (let [energy (get-in world [:options :plant-energy] *default-plant-energy*)
        grown (map #(Plant. (rnd-location %) energy) areas)]
    (reduce (fn [world plant] (assoc-in world [:plants (:location plant)] plant)) world grown)))


;; *Animals*

(defn move
  "Moves the given _animal_ to its new position.  
Return: `[world updated-animal]`"
  [world {:keys [location direction energy] :as animal}]
  (let [height (get-in world [:options :height] *default-height*)
        width (get-in world [:options :width] *default-width*)
        [y x] ((get directions direction) location)
        new-location [(mod y height) (mod x width)] ;wraps the location
        animal (-> (assoc animal :location new-location)
                   (assoc :energy (dec energy)))]
    [world animal]))

(defn turn
  "Turns the _animal_ to a new direction based on its _genes_.  
Return: `[world updated-animal]`"
  [world {:keys [genes direction] :as animal}]
  (let [sum (rand-int (apply + genes))
        angle (loop [genes genes sum sum dir 0]
                (let [turns (- sum (first genes))]
                  (if (neg? turns)
                    dir
                    (recur (rest genes) turns (inc dir)))))
        dir (mod (+ direction angle) (count genes))
        animal (assoc animal :direction dir)]
    [world animal]))

(defn eat
  "Transfers the _plants_ energy to the _animal_. It's also called eating.
During this action the _plant_ will be removed from the _world_.  
Return: `[world-without-eaten-plants updated-animal]`"
  [{plants :plants :as world} {:keys [location energy] :as animal}]
  (let [plant-at-position (get plants location (Plant. location 0))
        animal (assoc animal :energy (+ energy (:energy plant-at-position)))
        world (assoc world :plants (dissoc plants location))]
    [world animal]))

(defn reproduce
  "Reproduces the _animal_ if it has enough energy left.
It will transfer half of its energy and its genes to the child. The genes will randomly mutate at one position.  
Return: `[world-with-child updated-animal]`"
  [{animals :animals :as world} {:keys [energy genes] :as animal}]
  (if (>= energy (get-in world [:options :reproduction-energy] *default-reproduction-energy*))
    (let [new-energy (int (/ energy 2))
          parent (assoc animal :energy new-energy)
          mutation (rand-int (count genes))
          gene (+ (get genes mutation) (- (rand-int 3) 1))
          new-genes (assoc genes mutation (if (neg? gene) 0 gene))
          child (-> (assoc parent :genes new-genes)
                    (assoc-in [:stats :age] 0))
          new-world (conj-in world :animals [child])]
      [new-world parent])
    [world animal]))

(defn update-animal-stats
  "Updates the animal stats like age etc."
  [world {:keys [stats] :as animal}]
  [world (assoc animal :stats (assoc stats :age (inc (get stats :age 0))))])

(defn handle-animals
  "Handles the movement, eating, reproduction, etc. of the given _animals_ in the world.  
Return: `updated-world`"
  [{animals :animals :as past-world}]
  (loop [world (assoc past-world :animals []) animals animals]
    (if (empty? animals)
      world
      (let [animal (first animals)]
        (if (pos? (:energy animal))
          (let [[world animal] (->> (turn world animal)
                                    (apply move)
                                    (apply eat)
                                    (apply reproduce)
                                    (apply update-animal-stats))]
            (recur (conj-in world :animals [animal]) (rest animals)))
          (recur world (rest animals)))))))

(defn update-world-stats
  "Updates the stats of the world like its age."
  [{:keys [stats] :as world}]
  (assoc world :stats (assoc stats :age (inc (get stats :age 0)))))

(defn update-world
  "Evaluates the _world_ to the next state.  
Return: `world-in-next-state`"
  [world]
  (-> (handle-animals world)
      (add-plants)
      (update-world-stats)))

