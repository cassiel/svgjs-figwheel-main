(defproject net.cassiel/svg "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojure/clojurescript "1.11.60"]
                 [org.clojure/core.async "1.5.648"]
                 [com.stuartsierra/component "1.1.0"]
                 [net.cassiel/lifecycle "0.1.0-SNAPSHOT"]]
  :resource-paths ["target" "resources"]
  :clean-targets  ^{:protect false} ["target"]
  :plugins [[com.github.liquidz/antq "RELEASE"]]
  :aliases {"fig"    ["trampoline" "run" "-m" "figwheel.main"]
            "fig:ws" ["run" "-m" "figwheel.main" "-O" "whitespace" "-bo" "dev"]}
  :profiles {:dev {:dependencies   [[com.bhauman/figwheel-main "0.2.18"]
                                    [com.bhauman/rebel-readline-cljs "0.1.4"]]}})
