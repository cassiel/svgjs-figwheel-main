(ns net.cassiel.svg.forms.mask-experiment
  "Simple experiments with masking and/or clipping."
  (:require [net.cassiel.svg.protocols :as px]
            [goog.string :as gstring]
            [goog.string.format]))

(deftype MaskExperiment []
  px/FORM
  (render [this container size form-state]
    (let [mask-grad (-> (.gradient (.root container) "linear" #(doto %
                                                                 (.stop 0 "black")
                                                                 (.stop 1 "white")))
                        (.from 0 0.5)
                        (.to 1 0.5))
          g (.group container)]
      (-> g
          (.circle (* size 0.9))
          (.center (/ size 2) (/ size 2))
          (.stroke #js {:color "black"
                        :width 5})
          (.fill "#D0D0D0"))
      (-> g
          (.rect (* size 0.4) (* size 0.7))
          (.center (/ size 2) (/ size 2))
          (.rotate 30)
          (.fill "#F03000"))
      (let [r (-> container
                  (.rect size (* size 0.2))
                  (.center (/ size 2) (/ size 2))
                  #_ (.dy (/ size 4))
                  (.fill mask-grad))
            m (.mask container)]
        ;; This seems to remove the drawn "r" from the display:
        (.add m r)
        (.maskWith g m)
        (swap! form-state assoc
               :RECT r
               :SIZE size))))

  (tick [this container ts form-state]
    (let [r (:RECT @form-state)
          size (:SIZE @form-state)
          xt (- (* (rand) 200) 100)
          yt (- (* (rand) 200) 100)]
      (-> r
          (.animate 500)
          (.transform #js {:translate #js [xt yt]
                           :rotate (rand 360)})))))