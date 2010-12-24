(ns de.herrnorbert.EvolutionLoop.core.core
  "The core namespace of this little game.
The main animal functions return a vector like '[updated-world animal]'. This vector is shifted from one function, like 'eat', to the other functions, like 'reproduce'.

Evaluate the world to the next state with 'update-world':
> (update-world default-world)"
  (:use [de.herrnorbert.EvolutionLoop.core.locations :only [rnd-location directions]])
  (:use [de.herrnorbert.EvolutionLoop.core.utils :only [rnd-int-vec conj-in]]))

;; Some constants. This can be overwritten in the world struct.
(def *default-height* 100)
(def *default-width* 100)
(def *default-plant-energy* 80)
(def *default-reproduction-energy* 300)

;; A plant only consists of a location and some energy an animal can gain by eating it.
(defstruct plant :location :energy)

;; An animal consists of a location, energy (remaining turns it can survive
;; without eating), a direction and some genes which define the
;; direction in the next turn. Directions state the next cell the
;; animal will go to:
;; 0 1 2
;; 7 A 3
;; 6 5 4
(defstruct animal :location :energy :direction :genes)

;; The world 
(defstruct world :animals :plants :areas :options)

;; A default world would look like:
(def default-world (struct world
                           [(struct animal [50 50] 1000 0 (rnd-int-vec 8 10))]
                           []
                           [[0 0 100 100] [45 45 10 10]]
                           {:width  100
                            :height 100
                            :plant-energy 80
                            :reproduction-energy 300}))
;;
;; Plants
;;

(defn add-plants
  "Adds a randomly located plant in every given area and returns the updated world.
Return: '[world]'"
  [{:keys [plants areas] :as world}]
  (let [energy (get-in world [:options :plant-energy] *default-plant-energy*)
        grown (map #(struct plant (rnd-location %) energy) areas)]
    (conj-in world :plants grown)))

;;
;; Animals
;;

(defn move
  "Moves the given animal to its new position.
Return: '[world updated-animal]'"
  [world {:keys [location direction energy] :as animal}]
  (let [height (get-in world [:options :height] *default-height*)
        width (get-in world [:options :width] *default-width*)
        [y x] ((get directions direction) location)
        new-location [(mod y height) (mod x width)] ;wraps the location
        animal (-> (assoc animal :location new-location)
                   (assoc :energy (dec energy)))]
    [world animal]))

(defn turn
  "Turns the animal to a new direction based on its genes.
Return: '[world updated-animal]'"
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
  "Transfers the plants energy to the animal. It's also called 'eating'.
During this action the plant will be removed from the world.
Return: [world-without-eaten-plants updated-animal]"
  [{plants :plants :as world} {:keys [location energy] :as animal}]
  (let [at-position (filter #(= location (:location %)) plants)
        eatable-energy (reduce + (map :energy at-position))
        animal (assoc animal :energy (+ energy eatable-energy))
        world (assoc world :plants (remove #(= location (:location %)) plants))]
    [world animal]))

(defn reproduce
  "Reproduces the animal if it has enough energy left.
It will transfer half of its energy and its genes to the child. The genes will randomly mutate at one position.
Return: [world-with-child updated-animal]"
  [{animals :animals :as world} {:keys [energy genes] :as animal}]
  (if (>= energy (get-in world [:options :reproduction-energy] *default-reproduction-energy*))
    (let [new-energy (/ energy 2)
          parent (assoc animal :energy new-energy)
          mutation (rand-int (count genes))
          new-genes (assoc genes mutation (+ (get genes mutation) (- (rand-int 3) 1)))
          child (assoc parent :genes new-genes)
          new-world (conj-in world :animals child)]
      [new-world parent])
    [world animal]))

(defn handle-animals
  "Handles the movement, eating, reproduction, etc. of the given animals in the world.
Return: updated-world"
  [{animals :animals :as past-world}]
  (loop [world (assoc past-world :animals []) animals animals]
    (if (empty? animals)
      world
      (let [animal (first animals)]
        (if (pos? (:energy animal))
          (let [[world animal] (->> (turn world animal)
                                    (apply move)
                                    (apply eat)
                                    (apply reproduce))]
            (recur (conj-in world :animals animal) (rest animals)))
          (recur world (rest animals)))))))

(defn update-world
  "Evaluates the world to the next state.
Return: world-in-next-state"
  [world]
  (-> (handle-animals world)
      (add-plants)))

