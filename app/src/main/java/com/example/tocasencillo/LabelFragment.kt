package com.example.tocasencillo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tocasencillo.AssemblyActivity.Companion.delete
import com.example.tocasencillo.AssemblyActivity.Companion.id_song_frag
import com.example.tocasencillo.EditorActivity.Companion.label
import com.example.tocasencillo.EditorActivity.Companion.posic
import com.example.tocasencillo.EditorActivity.Companion.saving
import com.example.tocasencillo.MySQLiteHelper.Companion.LABEL_TABLE
import com.example.tocasencillo.databinding.FragmentLabelBinding

class LabelFragment : Fragment() {

    private val myValue = label

    private var _binding: FragmentLabelBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val myPosic = posic

    private var myId: Int = 0

    private val myTableId: Int = id_song_frag

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLabelBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.tvLabel.text = myValue

        myId = when (label) {
            getString(R.string.intro) -> {
                1
            }
            getString(R.string.verse) -> {
                2
            }
            getString(R.string.pre_chorus) -> {
                3
            }
            getString(R.string.chorus) -> {
                4
            }
            getString(R.string.bridge) -> {
                5
            }
            getString(R.string.solo) -> {
                6
            }
            else -> {
                7
            }
        }

        return view
    }

    override fun onStop() {
        if (delete){
            MySQLiteHelper(this.requireContext()).apply {
                deletePrefabs(LABEL_TABLE, myTableId )
            }
        }
        byeFragLabel()
        super.onStop()
    }

    private fun byeFragLabel() {
        if (!saving) {
            posic--
        } else {
            MySQLiteHelper(this.requireContext()).apply {
                saveSongFragment(
                    lastSong(),
                    LABEL_TABLE,
                    myPosic
                )
                saveLabel(lastSongFragment(), myId)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}