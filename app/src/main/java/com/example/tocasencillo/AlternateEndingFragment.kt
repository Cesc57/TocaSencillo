package com.example.tocasencillo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.tocasencillo.EditorActivity.Companion.posic
import com.example.tocasencillo.EditorActivity.Companion.saving
import com.example.tocasencillo.MySQLiteHelper.Companion.ALTERNATE_ENDING_TABLE
import com.example.tocasencillo.databinding.FragmentAlternateEndingBinding

class AlternateEndingFragment : Fragment() {

    private var _binding: FragmentAlternateEndingBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val myPosic = posic

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlternateEndingBinding.inflate(inflater, container, false)
        val view = binding.root

        val bar2: TextView = binding.tvLine2
        bar2.setOnClickListener {
            when (bar2.text) {
                "|" -> {
                    bar2.text = ":|"
                }
                else -> bar2.text = "|"
            }
        }

        return view
    }

    override fun onStop() {
        byeFragAlternateEnding()
        super.onStop()
    }

    private fun byeFragAlternateEnding() {
        if (saving) {
            val ccBar2: String = binding.tvLine2.text.toString()
            val txtCC1: String = binding.etBar1.text.toString()
            val txtCC2: String = binding.etBar2.text.toString()
            MySQLiteHelper(this.requireContext()).apply {
                saveSongFragment(lastSong(),
                    ALTERNATE_ENDING_TABLE,
                    myPosic)
                saveAlternateEnd(ccBar2, txtCC1, txtCC2, lastSongFragment())
            }
        } else {
            posic--
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}