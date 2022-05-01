package com.example.tocasencillo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tocasencillo.EditorActivity.Companion.posic
import com.example.tocasencillo.EditorActivity.Companion.saving
import com.example.tocasencillo.MySQLiteHelper.Companion.NOTE_TABLE
import com.example.tocasencillo.databinding.FragmentNoteBinding

class NoteFragment : Fragment() {

    private var _binding: FragmentNoteBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val myPosic = posic

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStop() {
        byeFragNote()
        super.onStop()
    }

    private fun byeFragNote() {
        if (saving) {
            val note: String = binding.txtNote.text.toString()
            MySQLiteHelper(this.requireContext()).apply {
                saveSongFragment(lastSong(),
                    NOTE_TABLE,
                    myPosic)
                saveNote(note, lastSongFragment())
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