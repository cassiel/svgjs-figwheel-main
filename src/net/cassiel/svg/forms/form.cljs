(ns net.cassiel.svg.forms.form
  "The actual SVG form, drawn into a provided SVG.js container
   assumed to be a square, with size provided."
  (:require [net.cassiel.svg.protocols :as px]
            [goog.string :as gstring]
            [goog.string.format])
  )

(def TEXT-CLASSES ["CLOCK-A" "CLOCK-B"])

(defn add-clock-fields [g size]
  (doseq [class TEXT-CLASSES]
    (-> g
        (.plain "X")                    ; Needed for centre-alignment.
        (.addClass class)
        (.font #js {"family" "Microgramma Bold"
                    "size"   (/ size 16)
                    "anchor" "middle"})
        (.fill "#FFFFFF")
        (.stroke #js {:color "#000000" :width 1})
        (.center (/ size 2) (* size 0.8))
        (.clear)))
  g)

(defn clock-from-ts
  "Clock display from msec timestamp"
  [ts]
  (let [d (js/Date. ts)
        ss (.getSeconds d)
        mm (.getMinutes d)
        hh (.getHours d)]
    (gstring/format "%02d:%02d:%02d" hh mm ss)
    )
  )

(deftype DemoForm []
  px/FORM
  (render [this container size form-state]
    (let [;; Gradients seem to get hoisted to the root anyway, but for clarity:
          disc-grad (-> (.gradient (.root container) "linear" #(doto %
                                                                 (.stop 0 "#303030")
                                                                 (.stop 0.5 "#FFBB80")
                                                                 (.stop 1 "#CC0000")))
                        (.from 0.5 0.2)     ; GUESS: normalised coordinates, 0..1,
                        (.to 0.5 0.8))

          rect-grad (-> (.gradient (.root container) "linear" #(doto %
                                                                 (.stop 0 "#F0C040")
                                                                 (.stop 1 "#C09010")))
                        (.from 0 0.2)
                        (.to 0 0.8))

          text-grad (-> (.gradient (.root container) "linear" #(doto %
                                                                 (.stop 0 "#000000")
                                                                 (.stop 0.5 "#FFFFFF")
                                                                 (.stop 1 "#202080")))
                        ;; Build a gradient TR to BL, to allow for later form rotation.
                        (.from 0.9 0.1)       ; Y grows downwards.
                        (.to 0.1 0.9))
          g         (.group container)    ; I don't think the creating element matters.
          ]
      (reset! form-state {:text-index 0})
      (-> g
          (.stroke #js {:color   "#000000"
                        :opacity 0
                        :width   2})
          (.animate 2000)
          (.after #(-> g
                       (.animate 500)
                       (.stroke #js {:opacity 0.5}))))

      (-> container
          (.circle (* size 0.9))
          (.fill disc-grad)
          (.stroke #js {:color   "#000000"
                        :opacity 0.3
                        :width   2})
          (.center (/ size 2) (/ size 2)))
      (-> g
          (.rect (/ size 2) (/ size 2))
          (.radius 10)                    ; Rounded corners.
          (.fill rect-grad)
          #_ (.stroke #js {:color   "#000000"
                           :opacity 0.5
                           :width   2})
          (.center (/ size 2) (/ size 2))
          ;; Rotates can be accumulated; it appears that translates can't.
          (.rotate 35)
          (.rotate 10)
          (.rotate -45))
      (-> g
          (.text "HELLO\nSVG\nWORLD")
          (.font #js {"family" "Microgramma Bold"
                      "size"   (/ size 13)
                      "anchor" "middle"})
          (.fill text-grad)
          ;; .size() doesn't seem to work, at least for fonts,
          ;; although including it in a map (above)
          ;; works fine, as does setting font-size via .attr().
          #_ (.size (/ size 12))
          #_ (.attr "font-size" (/ size 12))
          (.center (/ size 2) (/ size 2))
          #_ (.animate #js {:duration 2000})
          (.animate 500 "circInOut")
          #_ (.ease "<>")
          (.rotate -45)
          (.after #(println "DONE")))
      (add-clock-fields g size)
      #_ (-> g
             (.plain "X")                    ; Needed for centre-alignment.
             (.addClass TEXT-CLASS)
             (.font #js {"family" "Microgramma Bold"
                         "size"   (/ size 18)
                         "anchor" "middle"})
             (.fill "#FFFFFF")
             (.stroke #js {:color "#000000" :width 1})
             (.center (/ size 2) (* size 0.8))
             (.clear))

      (.addTo g container)))

  (tick [this container ts form-state]
    (let [ts' (+ ts 1000)
          ;; Animate to this time over 1 second.
          text-index (:text-index (swap! form-state update :text-index #(- 1 %)))
          this-text-node (.find container (str \. (nth TEXT-CLASSES text-index)))
          next-text-node (.find container (str \. (nth TEXT-CLASSES (- 1 text-index))))]
      (-> this-text-node
          (.animate #js {:when "now" :delay 250 :duration 750})
          (.fill #js {:opacity 0.0})
          (.stroke #js {:opacity 0.0}))
      (-> next-text-node
          (.plain (clock-from-ts ts'))
          (.animate #js {:when "now" :delay 250 :duration 750})
          (.fill #js {:opacity 1.0})
          (.stroke #js {:opacity 1.0})))))
