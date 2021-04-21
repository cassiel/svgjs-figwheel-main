(ns net.cassiel.svg.form
  "The actual SVG form, drawn into a provided SVG.js container
   assumed to be a square, with size provided.")

(defn render
  "Functional style. Return an isolated form."
  [container size]
  (let [;; Gradients seem to get hoisted to the root anyway, but for clarity:
        disc-grad (-> (.gradient (.root container) "linear" #(doto %
                                                               (.stop 0 "#303030")
                                                               (.stop 0.5 "#FFA080")
                                                               (.stop 1 "#CC0000")))
                      (.from 0.5 0.2)     ; GUESS: normalised coordinates, 0..1
                      (.to 0.5 0.8))

        text-grad (-> (.gradient (.root container) "linear" #(doto %
                                                               (.stop 0 "#FF4040")
                                                               (.stop 1 "#202080")))
                      )
        ]
    (-> container
        (.circle (* size 0.9))
        (.fill disc-grad)
        (.center (/ size 2) (/ size 2)))
    (-> container
        (.rect (/ size 2) (/ size 2))
        (.fill "#F0F0F0")
        (.center (/ size 2) (/ size 2))
        (.rotate 35)
        (.rotate 10)
        ;; Can't cascade transforms - I assume it's a single matrix construction
        ;; - although the two rotates above accumulate fine.
        #_ (.translate 100 0)
        ;; No joy with relative: true either
        #_ (.transform #js {:translate #js [0 200]
                         :relative true})
        ;; dmove is happy:
        #_ (.dmove 100 0)
        ;; This also accumulates fine!:
        (.rotate -45))
    (-> container
        (.text "HELLO\nSVG\nWORLD")
        (.font #js {"family" "Microgramma Bold"
                    "size"   (/ size 12)
                    "anchor" "middle"})
        (.fill text-grad)
        ;; .size() doesn't seem to work, although including it in a map (above)
        ;; works fine, as does setting font-size via .attr().
        #_ (.size 100)
        #_ (.attr "font-size" 80)
        (.center (/ size 2) (/ size 2))
        (.rotate -45))))
