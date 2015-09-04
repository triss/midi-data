;; MIDI data is often presented as a list of three values: `[a b c]`.
;; 
;; This namespace handle's parsing of such MIDI data tuples to maps containing
;; friendlier representation of the events they represent.
;;  
;; The values `[a b c]` encode encode the following:
;;
;; - `a` - *message type* and some times a *channel* for that message type.
;; - `b` - *note* or *paramater number* for message.
;; - `c` - *velocity* or *value* for message.
;;
;; ## Usage:
;;
;; - `(decode [a b c])` translates from MIDI data tuple to map.
;; - `(encode m)` translates from map back to a MIDI data tuple.

(ns midi-data.core
  (:require [clojure.set :refer [map-invert]]))

;; ## MIDI Message types

(def msg-type->n 
  "Maps MIDI message types to the numbers used to represent them."
  {:clock    0xF8 :active   0xFE :sysex      0xF0 :eox     0xF7 
   :start    0xFA :stop     0xFC :continue   0xFB :reset   0xFF 
   :note-on  0x90 :note-off 0x80 :channel-at 0xD0 :poly-at 0xA0 
   :program  0xC0 :control  0xB0 :pitchbend  0xE0 :mtc     0xF1 
   :song-pos 0xF2 :song-sel 0xF3 :tune       0xF6})

(def inverse-n->msg-type 
  "Map of numbers to midi msg types. Channel numbers often need to be removed
  with a fn like quantize before they can be looked up using this."
  (map-invert msg-type->n))

(defn n->msg-type 
  "Returns key representing meaning of numerical value. If n has channel number
  encoded within it it is discarded."
  [n] 
  (or (inverse-n->msg-type n) 
      (-> n (bit-and-not 0xF) inverse-n->msg-type)))

;; ### MIDI Message paramater types

(def msg-type->msg-params 
  "Maps midi message type to a to the names that `b` and `c` paramater values
  represent."
  {:note-on [:note :velocity] :note-off [:note :velocity]
   :control [:number :value]})

(defn n->channel 
  "Returns channel indicated by value. Typically used on first value of MIDI
  data tuple."
  [n] (bit-and n 0x0F))

;; ## Decodeing

(defn decode
  "Decodes MIDI message and transforms it to a map containing a representation
  of the MIDI message being sent."
  [[a b c :as data]] 
  (let [msg-type (n->msg-type a)] 
    (merge {:msg-type msg-type :channel (n->channel a)}
           (or (zipmap (msg-type->msg-params msg-type) [b c])
               {:data data}))))

;; ## Encoding

(defn encode 
  "Takes representation of MIDI event provided as a map and turns it back to
  a data tuple of `[a b c]`"
  [{:keys [msg-type channel] :or {channel 0} :as m}]
  (cons (+ (msg-type->n msg-type) channel) 
        ((apply juxt (msg-type->msg-params msg-type)) m)))
