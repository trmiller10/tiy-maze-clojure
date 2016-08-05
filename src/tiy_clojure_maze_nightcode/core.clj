(ns tiy-clojure-maze-nightcode.core
  (:gen-class))

(def size 10)

(defn create-rooms []
  (vec
    ;this for loop creates a list of 10 rows
    (for [row (range size)]
      (vec
        ;within each row, this for loop creates a list of 10 columns 
        (for [col (range size)]
          ;each column is given a hash-map that binds keys
          {:row row, 
           :col col, 
           :visited? false, 
           :bottom? true, 
           :right? true,
           :start? false,
           :finish? false})))))
           
        


;returns true if room exists (value of room does not equal nil) and has not been visited
(defn valid-unvisited-room? [room]
    (and room (= false (:visited? room))))
(defn dead-end-room? [rooms row col])
  

(defn possible-neighbors [rooms row col]
  ;returns a vector 
  (vec
    ;returns a collection of rooms that exist and have not been visited
    (filter valid-unvisited-room?
      [(get-in rooms [(dec row) col])
       (get-in rooms [(inc row) col])
       (get-in rooms [row (dec col)])
       (get-in rooms [row (inc col)])])))

(defn random-neighbor [rooms row col] 
  ;let binds neighbors to the result of running possible-neighbors on the current room
  (let [neighbors (possible-neighbors rooms row col)]
    ;if the count of neighbors is positive (true)
    (if (pos? (count neighbors))
     ;choose a random neighbor
     (rand-nth neighbors)
     ;if count of neighbors is 0 (false), mark room as dead-end and return nil
    
     nil)))

      

(defn tear-down-wall [rooms old-row old-col new-row new-col]
  ;cond evaluates each test in turn, first test that returns true has its associated
  ;expression evaluated and cond quits testing
 (cond                                                                          ;_
    ; going up                                                               1 |^|
    ;if new-row is less than old-row, then remove the new room's bottom wall 2 |_|
    (< new-row old-row) (assoc-in rooms [new-row new-col :bottom?] false)        ;_
    ; going down                                                               ;2|v| 
    ;if new-row is greater than old-row, then remove the old room's bottom wall 3|_|
    (> new-row old-row) (assoc-in rooms [old-row old-col :bottom?] false)
    ; going left
    (< new-col old-col) (assoc-in rooms [new-row new-col :right?] false)
    ; going right
    (> new-col old-col) (assoc-in rooms [old-row old-col :right?] false)))

;prepares a name for a binding in advance, even if it does not exist yet
(declare create-maze)

(defn create-maze-loop [rooms old-row old-col new-row new-col]
  ;creates a duplicate maze of rooms that tears down the same wall of room passed in from original rooms
  (let [new-rooms (tear-down-wall rooms old-row old-col new-row new-col)
        ;then runs create-maze on itself passing the new room coordinates in as the to-be current room coordinates
        new-rooms (create-maze new-rooms new-row new-col)]
    ;if it finds that the duplicate new-rooms maze matches the original rooms maze (a dead-end)
    (if (= rooms new-rooms)
      ;then it returns the original rooms maze (essentially backing up so it can find a new unvisited room to burrow into)
      rooms
      ;if the two mazes do not match, it runs itself again using the duplicate new-rooms maze as the to-be original rooms maze 
      (create-maze-loop new-rooms old-row old-col new-row new-col))))

;if the collection returned by filtering through the flattened rooms for rooms where :finish? is true is empty, return true
(defn last-room? [rooms row col] (empty? (filter :finish? (flatten rooms))))

(defn create-maze [rooms row col]
  ;mark the current room as visited
  (let [rooms (assoc-in rooms [row col :visited?] true)
        ;mark starting room
        rooms (assoc-in rooms [0 0 :start?] true)
        ;find the next room by running random-neighbor
        next-room (random-neighbor rooms row col)]
    ;if next-room exists (true)
    (if next-room
      ;remove current room's wall by passing current room's coordinates and retrieving next room's coordinates
      (let [rooms (tear-down-wall rooms row col (:row next-room) (:col next-room))]
        ;then run create-maze-loop using current room as old room and next-room as current room
        (create-maze-loop rooms row col (:row next-room) (:col next-room)))
      
      (if (last-room? rooms row col)
       (assoc-in rooms [row col :finish?] true)
       rooms))))



(defn -main []
  (let [rooms (create-rooms)
        rooms (create-maze rooms 0 0)]
    ; doseq repeatedly executes 
    (doseq [_ rooms]
      ;prints out the top wall for each first-line room in rooms
      (print " _"))
    ;pops down to next line
    (println)
    ; doseq repeatedly executes over each ROW in ROOMS
    (doseq [row rooms]
      ;first prints out the left wall for each room
      (print "|")
      ;nested doseq here repeatedly executes over each ROOM in the current ROW 
      (doseq [room row]
        
        (cond
          (and (:start? room) (:bottom? room)) (print "⍶")
          (:start? room) (print "α")
          (and (:finish? room) (:bottom? room)) (print "⨱")
          (:finish? room) (print "x")
          (:bottom? room) (print "_") :else (print " "))
        (if (:right? room) (print "|") (print " ")))
      (println))))
                      

                               
                              
                               
                                
                                

      
                             
        
        
;(if (:start? room)
          ;true
          ;if bottom true, print ⍶
  ;(if (:bottom? room)
 ;   (print "⍶")
   ; (if (:right? room)
    ;  (print "α")))
          ;false
          ;if bottom false, 
          ;tests if room is demarcated as having a bottom wall
  ;(if (:bottom? room)
          ;if true, prints the bottom wall
  ; (print "_")
          ;if false, prints a space
   ;(print " ")))       
        ;tests if room is demarcated as having a right wall
;(if (:right? room)
          ;if true, prints the right wall
 ; (print "|")
          ;if false, prints a space
  ;(print " "))
      ;pops down to next line
;(println)