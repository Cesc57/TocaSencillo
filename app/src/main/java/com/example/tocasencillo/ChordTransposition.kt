package com.example.tocasencillo

class ChordTransposition {

    fun newChords(oldChord: String, transpose: Int, decisionTension: String): String {

        val sharpNotes: Array<String> =
            arrayOf("A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#")
        val flatNotes: Array<String> =
            arrayOf("A", "Bb", "B", "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab")

        var note = ""
        var tension = ""
        var rest = ""
        var finalNote: String
        var positionArray: Int
        val chordResult: String

        if (oldChord.isNotEmpty()) {

            var loopFor = 0
            for (char in oldChord) {
                if (loopFor == 0 && char == 'A' || char == 'B' || char == 'C' || char == 'D' || char == 'E' || char == 'F' || char == 'G') {
                    note = char.toString()
                } else if (loopFor == 1 && char == '#' || char == 'b') {
                    tension = char.toString()
                } else {
                    rest += char.toString()
                }
                loopFor++
            }

            if (note != "") {
                finalNote = note + tension

                if (decisionTension == "#") {
                    positionArray = 0
                    for (elem in sharpNotes) {
                        if (elem == finalNote || finalNote == flatNotes[positionArray]) {
                            break
                        }
                        positionArray++
                    }

                    finalNote = sharpNotes[(24 + positionArray + transpose) % 12]

                } else if (decisionTension == "b") {
                    positionArray = 0
                    for (elem in flatNotes) {
                        if (elem == finalNote || finalNote == sharpNotes[positionArray]) {
                            break
                        }
                        positionArray++
                    }
                    finalNote = flatNotes[(24 + positionArray + transpose) % 12]
                }

                chordResult = finalNote + rest
            } else {
                return oldChord
            }

        } else {
            return ""
        }
        return chordResult
    }
}
