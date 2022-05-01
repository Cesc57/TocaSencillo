package com.example.tocasencillo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tocasencillo.EditorActivity.Companion.posic
import com.example.tocasencillo.EditorActivity.Companion.saving
import com.example.tocasencillo.MySQLiteHelper.Companion.BOX_REPEAT_TABLE
import com.example.tocasencillo.databinding.FragmentBoxRepeatBinding


class BoxRepeatFragment : Fragment() {

    private var _binding: FragmentBoxRepeatBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val myPosic = posic

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBoxRepeatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStop() {
        byeFragBoxRepeat()
        super.onStop()
    }

    private fun byeFragBoxRepeat() {
        if (saving) {
            val note: String = binding.rptText.text.toString()
            MySQLiteHelper(this.requireContext()).apply {
                saveSongFragment(
                    lastSong(),
                    BOX_REPEAT_TABLE,
                    myPosic
                )
                saveBoxRepeat(note, lastSongFragment())
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