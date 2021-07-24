(ns normalize.core
  (:require
    [clojure.string :refer [replace lower-case]]
    #?(:cljs ["normalize-diacritics" :refer [normalize normalizeSync]])))

(defn escape-special-characters [url]
  (replace url #"[^a-zA-Z0-9\u00C0-\u017F\ ]" ""))

(defn deaccent [text]
  "Remove accent from string"
  #?(:cljs (let [normalized (java.text.Normalizer/normalize text java.text.Normalizer$Form/NFD)]
             (replace normalized #"\p{InCombiningDiacriticalMarks}+" ""))
     :clj  (normalizeSync text)))

(defn space->separator [text]
  (replace text #"[ |-]{1,}" "-"))

(defn cut-special-char [string]
  (replace string #"[^\w\s-]" ""))

(defn normalize-string [string]
  (-> string
    (str)
    (deaccent)
    (cut-special-char)
    (escape-special-characters)
    (space->separator)
    (lower-case)))
