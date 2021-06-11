(ns net.cassiel.svg.forms.mask-experiment
  "Simple experiments with masking and/or clipping."
  (:require [net.cassiel.svg.protocols :as px]
            [goog.string :as gstring]
            [goog.string.format]))

(deftype MaskExperiment-1 []
  px/FORM
  (render [this container size form-state]
    (let [mask-grad (-> (.gradient (.root container) "linear" #(doto %
                                                                 (.stop 0 "black")
                                                                 (.stop 1 "white")))
                        (.from 0 0.5)
                        (.to 1 0.5))
          g         (.group container)]
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
    (let [r    (:RECT @form-state)
          size (:SIZE @form-state)
          xt   (- (* (rand) 200) 100)
          yt   (- (* (rand) 200) 100)]
      (-> r
          (.animate 500)
          (.transform #js {:translate #js [xt yt]
                           :rotate    (rand 360)})))))

(defn slat-form
  "Centre on (0, 0) within container."
  [& {:keys [container size slats thickness]}]
  (let [pitch   (/ size slats)
        top-pos (- (* (dec slats) pitch 0.5))
        g       (.group container)]
    (doseq [i (range slats)]
      (-> g
          (.rect size (* pitch thickness))
          (.center 0 (+ top-pos (* i pitch)))
          (.fill "#803030")))
    g))

(defn mask-form [& {:keys [container size]}]
  (let [g (.group container)]
    (-> g
        (.circle (* size 0.9))
        (.fill "white")
        (.center 0 0))
    (-> g
        (.circle (* size 0.7))
        (.center 0 0)
        (.fill "black"))
    g))

(deftype MaskExperiment-2 []
  px/FORM
  (render [this container size form-state]
    (let [centered (-> (.group container)
                       (.transform #js {:translate #js [(/ size 2) (/ size 2)]}))
          mask     (mask-form :container centered :size size)
          form     (slat-form :container centered
                              :size (* size 0.8)
                              :slats 3
                              :thickness 0.9)
          m        (.mask container)]
      (.add m mask)
      (.maskWith form m)))

  (tick [this container ts form-state]
    nil))
