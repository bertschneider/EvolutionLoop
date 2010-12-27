# EvolutionLoop

_EvolutionLoop_ is a little simulation game based on the one in the
great book [Land Of
Lisp](http://www.amazon.com/Land-Lisp-Learn-Program-Game/dp/1593272812/ref=sr_1_1?ie=UTF8&qid=1293460279&sr=8-1).

It simulates a world in which animals move around on their search of
food. As the world progresses they reproduce and mutate and eventually
build two new kinds of animals.

## Tests

Currently the tests aren't working. I tried `midje` but that didn't
work out for me. I will rewrite them with `LazyTest`in the future.

## Usage

A few GUIs are plant for this game. Currently only a _REPL_ GUI
exists but a _Swing_ and _Web_ GUI will follow.

### REPL 

The easiest way to start the simulation is to use the functions
provided in package
`de.herrnorbert.EvolutionLoop.gui-repl.gui-repl`. Furthermore there
are some functions to inspect the world and the animals in it.

     $ lein repl
     REPL started; server listening on localhost:34850.
     user=> (ns de.herrnorbert.EvolutionLoop.gui-repl.gui-repl)
     nil
     de.herrnorbert.EvolutionLoop.gui-repl.gui-repl=> (compile 'de.herrnorbert.EvolutionLoop.gui-repl.gui-repl)
     de.herrnorbert.EvolutionLoop.gui-repl.gui-repl
     de.herrnorbert.EvolutionLoop.gui-repl.gui-repl=> (evolve-times 100)
     Evaluating step  1
     Evaluating step  2
     Evaluating step  3
     ...
     Evaluating step  98
     Evaluating step  99
     Evaluating step  100
                                                                                           *      *      
                                             *                                     ** *                  
              *                                                   *              *     *                *
         *         *        *                                  A                   *           *         
      *                                **           A                       *                            
     *          *                               *                                                        
                                                 *                                                *      
                                                              *        *                                 
         *                                                                               *               
                                               A                                                         
                                           *        *  *               A              *                  
               *                         *        A *  *   * A                                           
                                              *  A     ** *                          *       *           
          *            *               *             *  *  *                                          *  
                                            *  *  A  *   *                                  *            
                              *           *    *  *             A                       *                
                                                  A * **             *                                   
                                                  * * A* *                A                 *            
          *               **                      A**  *  * A                             *          *   
                   *                                 *  * *      *                     *                 
                 *                               A                    *   A                        *     
                                    *                             A                             *        
        *    *          *                        *                                                       
                                                          A                 A            *               
        **                                             *     *                                           
            *                      *                                                A          *    *    
                                  *                      *                          *                    
        *                                       *        *            *                     *           *
                              *       *                   *                 *             *             *
                               *                                                                         

## Documentation

Have a look at the _documentation_ in the `docs` folder. It's
poweredby [Marginalia](https://github.com/fogus/marginalia).

## License

Copyright (C) 2010 FIXME

Distributed under the Eclipse Public License, the same as Clojure.
