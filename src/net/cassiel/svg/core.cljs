(ns ^:figwheel-hooks net.cassiel.svg.core
  (:require [com.stuartsierra.component :as component]
            [net.cassiel.svg.components.dummy :as dummy]
            [net.cassiel.svg.components.resizer :as resizer]
            [net.cassiel.svg.components.clock :as clock]
            [net.cassiel.svg.components.svg :as svg]
            #_ [cljsjs.svgjs]))

(enable-console-print!)

(defn system []
  (component/system-map :dummy (dummy/map->DUMMY {})
                        :clock (clock/map->CLOCK {})
                        :svg (component/using (svg/map->SVG {})
                                              [:clock])
                        :resizer (component/using (resizer/map->RESIZER {})
                                                  [:svg])))

(defonce S (atom (system)))

(defn ^:before-load teardown []
  (swap! S component/stop))

(defn ^:after-load startup []
  (swap! S component/start))
