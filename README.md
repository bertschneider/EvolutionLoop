# EvolutionLoop

_EvolutionLoop_ is a little simulation game based on the one in the
great book [Land Of
Lisp](http://www.amazon.com/Land-Lisp-Learn-Program-Game/dp/1593272812/ref=sr_1_1?ie=UTF8&qid=1293460279&sr=8-1).

It simulates a world in which animals move around on their search of
food. As the world progresses they reproduce and mutate and eventually
build two new kinds of animals.

## Usage

The easies way to start the simulation is to use the functions
provided in the package
`de.herrnorbert.EvolutionLoop.gui-repl.gui-repl`. Furthermore there
are some functions to inspect the world and the animals in it.

   de.herrnorbert.EvolutionLoop.gui-repl.gui-repl> (time (evaluate-times 10000))
   Evaluating step  100
   Evaluating step  200
   Evaluating step  300
   Evaluating step  400
   ...
   Evaluating step  9900
   Evaluating step  10000
         *        * *                  A  A                 A                                          
   *    *         * *                 A         A                            A                         
        *          * * * *                                        A                     *              
                             *               A   A         A                                 A A       
                  *                A            A  A            A          A        *                  
                                                     AA    A               A                           
                                 *    *    *                   A                    *         *        
   A          A                                A              A   A                                    
                  *                                     A A        A                        A  *       
           *            A                        A A AA   AA A   A                                  *  
                                      *       A  A                                                     
        *                            *           A  A  A        A                  *            A      
      A                 A          A           AA AA    *                  *                A A        
      A                                        A *   A   A  A                           A              
                         A            A       A  *  A  *   A  A                        A           *   
                                    A      A       AAA * *                                             
          A                      A          AA    AAAA A*      A                                    A A
           A                 A        A A               *                                              
             AA                 *                 A*     *   *     A                      *            
           A           A     A                 A    A   *A                                             
                                          A    A    AA      A               *         **               
                      A   A  A              A                        A                   *             
                                                               *        A                      *       
                                         A         A              *    A            A   *    *         
      * *         *   A                       A  A       *                A                            
    A              *                                      A   *                                        
           A                       *          A                                     *       *    A     
   *                  *    A           *        A     A      A  AA       *                             
              A               * A                                      *    A      *                  *
                                            A         A              A            *                    


## Documentation

Have a look at the _documentation_ in the `docs`folder.

## License

Copyright (C) 2010 FIXME

Distributed under the Eclipse Public License, the same as Clojure.
