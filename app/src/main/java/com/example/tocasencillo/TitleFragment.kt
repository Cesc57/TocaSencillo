package com.example.tocasencillo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tocasencillo.EditorActivity.Companion.posic
import com.example.tocasencillo.EditorActivity.Companion.saving
import com.example.tocasencillo.MySQLiteHelper.Companion.TITLE_TABLE
import com.example.tocasencillo.databinding.FragmentTitleBinding

class TitleFragment : Fragment() {

    private var _binding: FragmentTitleBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val myPosic = posic

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTitleBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStop() {
        byeFragTitle()
        super.onStop()
    }

    private fun byeFragTitle() {
        if (saving) {
            val title: String = binding.title.text.toString()
            val tempo: String = binding.tempo.text.toString()
            val key: String = binding.tune.text.toString()
            MySQLiteHelper(this.requireContext()).apply {
                saveSongFragment(lastSong(),
                    TITLE_TABLE,
                    myPosic)
                saveTitle(title, tempo, key, lastSongFragment())
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