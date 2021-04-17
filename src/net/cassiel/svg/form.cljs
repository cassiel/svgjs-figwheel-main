(ns net.cassiel.svg.form
  "The actual SVG form, drawn into a provided SVG.js container
   assumed to be a square, with size provided.")

(defn render
  "Functional style. Return an isolated form."
  [size]
  (let [g (js/SVG.G.)]
    (-> g
        (.circle size)
        (.fill "#000000")
        (.center (/ size 2) (/ size 2)))
    (-> g
        (.rect (/ size 2) (/ size 2))
        (.fill "#F0F0F0")
        (.center (/ size 2) (/ size 2))
        (.rotate 35)
        (.rotate 10)
        ;; Can't cascade transforms - I assume it's a single matrix construction
        ;; - although the two rotates above accumulate fine.
        #_ (.translate 100 0)
        ;; No joy with relative: true either
        #_ (.transform #js {:translate #js [100 0]
                            :relative true})
        ;; dmove is happy:
        (.dmove 100 0)
        ;; This also accumulates fine!:
        (.rotate -45))
    g))
