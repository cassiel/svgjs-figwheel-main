(ns net.cassiel.svg.components.svg
  (:require-macros [cljs.core.async.macros :refer [go go-loop alt!]])
  (:require [com.stuartsierra.component :as component]
            [net.cassiel.lifecycle :refer [starting stopping]]
            [net.cassiel.svg.protocols :as px]
            [net.cassiel.svg.form :as form]
            [net.cassiel.svg.grass :as grass]
            [net.cassiel.svg.grid :as grid]
            [net.cassiel.svg.flow-field :as flow-field]
            [cljs.core.async :as a :refer [>! <!]]))

;;(def FORM #(form/DemoForm.))
;;(def FORM #(grid/GridForm.))
(def FORM #(flow-field/FlowFieldForm.))

(defn empty-svg! []
  (.empty (js/$ "svg.svgmain")))

(defn calculate-square-parameters
  "Calculate x, y and side length of maximum square in this SVG area."
  []
  (let [elem (js/$ "svg.svgmain")
        w (.innerWidth elem)
        h (.innerHeight elem)]
    (if (> w h)
      {:x (/ (- w h) 2)
       :y 0
       :size h}
      {:x 0
       :y (/ (- h w) 2)
       :size w})))

(defn render
  "Render the form. `form-state` is a convenience atom for state (initially nil).
   If resizing occurs, `render` will be called again with the preserved state,
   so should clear it down if necessary."
  [svg form form-state]
  (let [{:keys [x y size]} (calculate-square-parameters)
        svg' (-> (.nested svg) (.move x y))
        g (.group svg')]
    (px/render form g size form-state)))

(defn first-child [elem]
  (first (.children elem)))

(defn get-main-group
  "The actual content renders onto a group in a sub-SVG."
  [svg]
  (-> svg
      first-child                           ; Inner SVG; other children are <defs> etc.
      first-child                           ; The single child should be the group.
      ))

(defn tick
  "Clock tick on the second (or very shortly thereafter)."
  [svg ts form form-state]
  (px/tick form (get-main-group svg) ts form-state))

(defrecord SVG [clock svg form form-state installed?]
  Object
  (toString [this] (str "SVG " (seq this)))

  component/Lifecycle
  (start [this]
    (starting this
              :on installed?
              :action #(let [svg (js/SVG)
                             form (FORM)
                             form-state (atom nil)]
                         (-> svg
                             (.addTo "#main")
                             (.addClass "svgmain")
                             (.size "100%" "100%"))
                         (render svg form form-state)
                         (go-loop []
                           (when-let [v (<! (:tick-chan clock))]
                             (tick svg v form form-state)
                             (recur)))
                         (assoc this
                                :svg svg
                                :form form
                                :form-state form-state
                                :installed? true))))

  (stop [this]
    (stopping this
              :on installed?
              :action #(do
                         (.remove (js/$ "svg"))
                         (assoc this
                                :svg nil
                                :form nil
                                :form-state nil
                                :installed? false)))))
