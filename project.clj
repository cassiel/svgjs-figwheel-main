(defproject net.cassiel/svg "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.3"]]
  :profiles {:dev {:dependencies [[org.clojure/clojurescript "1.10.844"]
                                  [org.clojure/core.async "1.3.610"]
                                  [com.bhauman/figwheel-main "0.2.13"]
                                  ;; optional but recommended
                                  [com.bhauman/rebel-readline-cljs "0.1.4"]

                                  ;; [cljsjs/svgjs "2.2.5-0"] -- TOO OLD
                                  [com.stuartsierra/component "1.0.0"]
                                  [net.cassiel/lifecycle "0.1.0-SNAPSHOT"]

                                  [cljsjs/stats "16.0-0"]]
                   :resource-paths ["target"]
                   :clean-targets ^{:protect false} ["target"]}}
  :aliases {"fig" ["trampoline" "run" "-m" "figwheel.main"]})
