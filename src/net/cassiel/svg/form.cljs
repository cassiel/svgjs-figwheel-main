(ns net.cassiel.svg.form
  "The actual SVG form, drawn into a provided SVG.js container
   assumed to be a square, with size provided.")

(defn render
  "Functional style. Return an isolated form."
  [size]
  (let [g (js/SVG.G.)]
    (-> g
        (.circle size)
        (.fill "#000000"))
    (-> g
        (.rect (/ size 2) (/ size 2))
        (.fill "#FFFFFF")
        (.center (/ size 2) (/ size 2)))
    g))
