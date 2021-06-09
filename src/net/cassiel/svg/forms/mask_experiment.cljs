(ns net.cassiel.svg.forms.mask-experiment
  "Simple experiments with masking and/or clipping."
  (:require [net.cassiel.svg.protocols :as px]
            [goog.string :as gstring]
            [goog.string.format]))

(deftype MaskExperiment []
  px/FORM
  (render [this container size form-state]
    (let [g (.group container)]
      (-> g
          (.circle (* size 0.9))
          (.center (/ size 2) (/ size 2))
          (.fill "#D0D0D0"))
      (-> g
          (.rect (* size 0.7) (* size 0.7))
          (.center (/ size 2) (/ size 2))
          (.rotate 30)
          (.fill "#A0A0A0"))
      (let [r (-> container
                  (.rect size (* size 0.2))
                  (.center (/ size 2) (/ size 2))
                  (.fill "#FFF"))
            m (.mask container)]
        ;; This seems to remove the drawn "r" from the display:
        (.add m r)
        (.maskWith g m))))

  (tick [this container ts form-state]
    nil))
