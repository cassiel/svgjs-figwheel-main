(defproject svgjs-figwheel-main "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.3"]]
  :profiles {:dev {:dependencies [[org.clojure/clojurescript "1.10.773"]
                                  [com.bhauman/figwheel-main "0.2.11"]
                                  ;; optional but recommended
                                  [com.bhauman/rebel-readline-cljs "0.1.4"]

                                  [com.stuartsierra/component "1.0.0"]
                                  [net.cassiel/lifecycle "0.1.0-SNAPSHOT"]]
                   :resource-paths ["target"]
                   :clean-targets ^{:protect false} ["target"]}}
  :aliases {"fig" ["trampoline" "run" "-m" "figwheel.main"]})
