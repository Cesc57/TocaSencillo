package com.example.tocasencillo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tocasencillo.AssemblyActivity.Companion.delete
import com.example.tocasencillo.AssemblyActivity.Companion.positionInSong
import com.example.tocasencillo.MySQLiteHelper.Companion.NOTE_TABLE
import com.example.tocasencillo.databinding.FragmentNoteBuildBinding

class NoteBuildFragment : Fragment() {

    private val myPosic: Int = positionInSong

    private var _binding: FragmentNoteBuildBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteBuildBinding.inflate(inflater, container, false)
        val view = binding.root

        dataRecovery()

        return view
    }

    override fun onStop() {
        deleteFrag()
        super.onStop()
    }

    private fun deleteFrag() {
        if (delete) {
            MySQLiteHelper(this.requireContext()).apply {
                deleteFragment(myPosic, NOTE_TABLE)
            }
        }
    }

    private fun dataRecovery() {
        MySQLiteHelper(this.requireContext()).apply {
            binding.txtNote.text = searchNoteById(myPosic)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}