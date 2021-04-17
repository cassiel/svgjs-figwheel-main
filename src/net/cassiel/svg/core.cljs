(ns ^:figwheel-hooks net.cassiel.svg.core
  (:require [com.stuartsierra.component :as component]
            [net.cassiel.svg.components.dummy :as dummy]
            [net.cassiel.svg.components.resizer :as resizer]
            [net.cassiel.svg.components.svg :as svg]
            [net.cassiel.svg.components.form :as form]
            #_ [cljsjs.svgjs]))

(enable-console-print!)

(defn system []
  (component/system-map :dummy (dummy/map->DUMMY {})
                        :svg (svg/map->SVG {})
                        :form (form/map->FORM {})
                        :resizer (component/using (resizer/map->RESIZER {})
                                                  [:svg])))

(defonce S (atom (system)))

(defn ^:before-load teardown []
  (swap! S component/stop))

(defn ^:after-load startup []
  (swap! S component/start))
