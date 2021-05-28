(ns net.cassiel.svg.components.world
  (:require [com.stuartsierra.component :as component]
            [net.cassiel.lifecycle :refer [starting stopping]]))

(defrecord WORLD [scene renderer stopper stats installed?]
  Object
  (toString [this] (str "WORLD " (seq this)))

  component/Lifecycle
  (start [this]
    (starting this
              :on installed?
              :action #(let [scene (js/THREE.Scene.)
                             camera #_ (js/THREE.PerspectiveCamera. 75
                                                                 (/ (.-innerWidth js/window)
                                                                    (.-innerHeight js/window))
                                                                 0.1
                                                                 1000)
                             (js/THREE.OrthographicCamera. -2 2 -2 2 -10 10)
                             canvas (first (js/$ "<canvas/>" #js {:class "three-canvas"}))

                             _ (js/console.log canvas)
                             _ (-> (js/$ "#main") (.append canvas))
                             renderer (js/THREE.WebGLRenderer. #js {:canvas canvas})


                             geom (js/THREE.BoxGeometry. 4 4 4 10 20 25)
                             mat (js/THREE.MeshBasicMaterial. #js {:color 0xA0A0A0
                                                                   :wireframe true})
                             cube (js/THREE.Mesh. geom mat)

                             ;; --- content (haüy/form)
                             ;; An "alive" flag to let us kill the animation
                             ;; refresh when we tear down:
                             RUNNING (atom true)]
                        (.setSize renderer (.-innerWidth js/window) (.-innerHeight js/window))
                        #_ (.appendChild (.-body js/document) (.-domElement renderer))

                        (set! (.. scene -background) (js/THREE.Color. 0x202020))

                        (set! (.. camera -position -z) 1)

                        (.add scene cube)

                        (doto cube
                          (.rotateY 0.4)
                          (.rotateZ 0.2))

                        (letfn [(animate [n]
                                  (when @RUNNING (js/requestAnimationFrame
                                                  (partial animate (inc n))))

                                  #_ (set! (.. content -rotation -x) (/ n 100))

                                  (.render renderer scene camera)
                                  (when-let [stats (:stats stats)] (.update stats)))]
                          (animate 0))

                        (assoc this
                               :scene scene
                               :renderer renderer
                               :stopper (fn [] (reset! RUNNING false))
                               :installed? true))))

  (stop [this]
    (stopping this
              :on installed?
              :action #(do
                         (stopper)
                         #_ (.removeChild (.-body js/document) (.-domElement renderer))
                         (.remove (js/$ ".three-canvas"))
                         (assoc this
                                :stopper nil
                                :renderer nil
                                :scene nil
                                :installed? false)))))
