;; MIDI data is often presented as a list of three values: `[a b c]`.
;; 
;; This namespace handle's parsing of MIDI data tuples to maps that
;; represnt there content and back. 
;;  
;; The values `[a b c]` typically encode the following:
;;
;; - `a` - *message type* and some times a *channel* for that message type.
;; - `b` - *note* or *paramater number* for message.
;; - `c` - *velocity* or *value* for message.
;;
;; This namespace handles parsing of these to maps in the following format:
;; 
;; `{:midi-msg message-type ... message-type specific parsing tuple}`
;; 
;; Examples might be:
;; `{:midi-msg :note-on  :note 60 :vel 60 :channel 1}`
;; `{:midi-msg :note-off :note 60 :vel 60 :channel 1}`
;; `{:midi-msg :control :number 10 :value 60 :channel 1}`
;; `{:midi-msg :clock}`
;; `{:midi-msg :start}`
;; `{:midi-msg :stop}`
;;
;; ## Usage:
;;
;; - `(decode [a b c])` translates from MIDI data tuple to map.
;; - `(encode m)` translates from map back to a MIDI data tuple.

(ns midi-data.core
  (:require [clojure.set :refer [map-invert]]))

;; ## MIDI Message types

(def midi-msg-type->n 
  "Maps MIDI message types to the numbers used to represent them."
  {:clock    0xF8 :active   0xFE :sysex      0xF0 :eox     0xF7 
   :start    0xFA :stop     0xFC :continue   0xFB :reset   0xFF 
   :note-on  0x90 :note-off 0x80 :channel-at 0xD0 :poly-at 0xA0 
   :program  0xC0 :control  0xB0 :pitchbend  0xE0 :mtc     0xF1 
   :song-pos 0xF2 :song-sel 0xF3 :tune       0xF6})

(def inverse-n->midi-msg-type 
  "Map of numbers to midi msg types. Channel numbers often need to be removed
  with a fn like quantize before they can be looked up using this."
  (map-invert midi-msg-type->n))

(defn n->channel
  "Returns channel indicated by value. Typically used on first value of MIDI
  data tuple."
  [n] (bit-and n 0x0F))

(defn strip-channel 
  [n] (bit-and-not n 0xF))

(defn n->midi-msg-type
  "Returns key representing meaning of numerical value. If n has channel number
  encoded within it it is discarded."
  [n] 
  (or (inverse-n->midi-msg-type n) 
      (inverse-n->midi-msg-type (strip-channel n))))

;; ### MIDI Message paramater types

(def midi-msg-type->msg-params 
  "Maps midi message type to a to the names that `b` and `c` paramater values
  represent."
  {:note-on [:note :velocity] :note-off [:note :velocity]
   :control [:number :value]})

;; ## Decodeing

(defn decode
  "Decodes MIDI message and transforms it to a map containing a representation
  of the MIDI message being sent."
  [[a b c :as data]] 
  (let [msg-type (n->midi-msg-type a)] 
    (merge {:midi-msg msg-type :channel (n->channel a)}
           (or (zipmap (midi-msg-type->msg-params msg-type) [b c])
               {:data data}))))

;; ## Encoding

(defn encode 
  "Takes representation of MIDI event provided as a map and turns it back to
  a data tuple of `[a b c]`"
  [{:keys [midi-msg channel] :or {channel 0} :as m}]
  (cons (+ (midi-msg-type->n midi-msg) channel) 
        ((apply juxt (midi-msg-type->msg-params midi-msg)) m)))
