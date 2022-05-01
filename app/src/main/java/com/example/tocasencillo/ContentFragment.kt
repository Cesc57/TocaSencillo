package com.example.tocasencillo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.tocasencillo.EditorActivity.Companion.posic
import com.example.tocasencillo.EditorActivity.Companion.saving
import com.example.tocasencillo.MySQLiteHelper.Companion.CONTENT_TABLE
import com.example.tocasencillo.databinding.FragmentContentBinding

class ContentFragment : Fragment() {

    private var _binding: FragmentContentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val myPosic = posic

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentContentBinding.inflate(inflater, container, false)
        val view = binding.root

        /*the first and the last chord bar
         can be changed to repeat bars*/
        val bar1: TextView = binding.tvLine1
        bar1.setOnClickListener {
            when (bar1.text) {
                "|" -> {
                    bar1.text = "|:"
                }
                else -> bar1.text = "|"
            }
        }

        val bar5: TextView = binding.tvLine5
        bar5.setOnClickListener {
            when (bar5.text) {
                "|" -> {
                    bar5.text = ":|"
                    //bar5.autoSizeMaxTextSize()
                }
                else -> bar5.text = "|"
            }
        }

        return view
    }

    override fun onStop() {
        byeFragContent()
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun byeFragContent() {
        if (saving) {
            val ccBar1: String = binding.tvLine1.text.toString()
            val ccBar5: String = binding.tvLine5.text.toString()
            val txtCC1: String = binding.etBar1.text.toString()
            val txtCC2: String = binding.etBar2.text.toString()
            val txtCC3: String = binding.etBar3.text.toString()
            val txtCC4: String = binding.etBar4.text.toString()
            MySQLiteHelper(this.requireContext()).apply {
                saveSongFragment(
                    lastSong(),
                    CONTENT_TABLE,
                    myPosic
                )
                saveContent(ccBar1, ccBar5, txtCC1, txtCC2, txtCC3, txtCC4, lastSongFragment())
            }
        } else {
            posic--
        }
    }
}