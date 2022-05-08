package com.example.tocasencillo

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tocasencillo.databinding.ActivityChordsBinding

class ChordsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChordsBinding
    private var transpose = 0

    private val sharpNotes: Array<String> =
        arrayOf("A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#")
    private val flatNotes: Array<String> =
        arrayOf("A", "Bb", "B", "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab")

    private var note: String = "Z"
    private var tension: String = ""
    private var rest: String = ""
    private var finalNote: String = ""
    private var positionArray: Int = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChordsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnClear.setOnClickListener {
            binding.chordResult.text = ""
        }

        binding.btnTransform.setOnClickListener {
            transposeChord()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun transposeChord() {
        if (binding.chord.text.isNotEmpty()) {
            transpose = 0
            note = "Z"
            tension = ""
            rest = ""
            finalNote = ""
            positionArray = 1
            binding.chordResult.text = ""
            when {
                binding.rb1.isChecked -> {
                    transpose = 2
                }
                binding.rb2.isChecked -> {
                    transpose = 1
                }
                binding.rb3.isChecked -> {
                    transpose = -1
                }
                binding.rb4.isChecked -> {
                    transpose = -2
                }
            }

            for (char in binding.chord.text.toString()) {
                //binding.chordResult.text = """${binding.chordResult.text}$char,"""
                if (char == 'A' || char == 'B' || char == 'C' || char == 'D' || char == 'E' || char == 'F' || char == 'G') {
                    note = char.toString()
                } else if (char == '#' || char == 'b') {
                    tension = char.toString()
                } else {
                    rest += char.toString()
                }
            }

            finalNote = note + tension

            if (binding.rbSharp.isChecked) {
                positionArray = 0
                for (elem in sharpNotes) {
                    if (elem == finalNote || finalNote == flatNotes [positionArray]) {
                        break
                    }
                    positionArray++
                }

                finalNote = sharpNotes[(24 + positionArray + transpose) % 12]

            } else if (binding.rbFlat.isChecked) {
                positionArray = 0
                for (elem in flatNotes) {
                    if (elem == finalNote || finalNote == sharpNotes [positionArray]) {
                        break
                    }
                    positionArray++
                }
                finalNote = flatNotes[(24 + positionArray + transpose) % 12]
            }

            binding.chordResult.text = finalNote + rest

        } else {
            Toast.makeText(this, "Introduce un acorde", Toast.LENGTH_SHORT).show()
        }
    }
}