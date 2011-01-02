(defproject de.herrnorbert/EvolutionLoop "1.0.0-SNAPSHOT"
  :description "A little evolution simulator, like the one in Land of Lisp."
  :dependencies [[org.clojure/clojure "1.3.0-alpha4"]
                 [org.clojure.contrib/complete "1.3.0-alpha4"]
                 [com.stuartsierra/lazytest "2.0.0-SNAPSHOT"]
                 [incanter "1.2.3"]]
  :dev-dependencies [[marginalia "0.2.0"]
                     [swank-clojure "1.3.0-SNAPSHOT"]
                     [lein-lazytest "1.0.1"]]
  :lazytest-path ["src" "test"]
  :repositories {"clojure-releases" "http://build.clojure.org/releases"
                 "stuartsierra-releases" "http://stuartsierra.com/maven2"
                 "stuartsierra-snapshots" "http://stuartsierra.com/m2snapshots"})
