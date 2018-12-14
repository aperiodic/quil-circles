(ns quil-scratch.core
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(defn setup []
  ; Set frame rate to 30 frames per second.
  (q/frame-rate 30)
  ; Set color mode to HSB (HSV) instead of default RGB.
  (q/color-mode :hsb)
  ; setup function returns initial state. It contains
  ; circle color and position.
  {:color 0
   :angle 0})

(defn update-state [state]
  ; Update sketch state by changing circle color and position.
  {:color (mod (+ (:color state) 0.2) 255)
   :angle (+ (:angle state) 0.03)})

(defn invert-hue
  [h]
  (-> h (+ 128) (mod 255)))

(defn abs
  [x]
  (if (pos? x) x (- x)))

(defn draw-state [state]
  ; Clear the sketch by filling it with light-grey color.
  (q/background 240)
  (q/stroke-weight 5)

  ; Draw 10 circles
  (let [base-angle (:angle state)
        base-color (:color state)
        n 55
        step (/ 128.0 n)
        shift-col (fn [c off] (-> (+ c off) (mod 255)))]

    (q/with-translation [(/ (q/width) 4)
                         (/ (q/height) 4)]

      (doseq [i (range (inc n))]
        ; Draw the circle.
        (let [k 0.1
              x (* 150 (q/cos (* base-angle (* i k))))
              y (* 150 (q/sin (* base-angle (* i k))))
              r (- 330 (* i 12))
              col (shift-col base-color (* i step))]
          (q/fill col 200 216)
          (q/stroke (invert-hue col) 200 216)

          (q/ellipse (+ x (* 10 i))
                     (+ y (* 10 i))
                     (abs r)
                     (abs (- r
                             (-> (* i i)
                               (/ 15.0)))
                          )))))))

(q/defsketch quil-scratch
  :title "You spin my circle right round"
  :size [1024 1024]
  ; setup function called only once, during sketch initialization.
  :setup setup
  ; update-state is called on each iteration before draw-state.
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  ; This sketch uses functional-mode middleware.
  ; Check quil wiki for more info about middlewares and particularly
  ; fun-mode.
  :middleware [m/fun-mode])
