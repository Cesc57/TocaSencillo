package com.example.tocasencillo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tocasencillo.AssemblyActivity.Companion.delete
import com.example.tocasencillo.AssemblyActivity.Companion.id_song_frag
import com.example.tocasencillo.AssemblyActivity.Companion.myTension
import com.example.tocasencillo.AssemblyActivity.Companion.transposedChord
import com.example.tocasencillo.MySQLiteHelper.Companion.TITLE_TABLE
import com.example.tocasencillo.databinding.FragmentTitleBuildBinding

class TitleBuildFragment : Fragment() {

    private val myId: Int = id_song_frag
    private lateinit var data: ArrayList<String>
    private lateinit var transposition: ChordTransposition

    private var _binding: FragmentTitleBuildBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTitleBuildBinding.inflate(inflater, container, false)
        val view = binding.root

        dataRecovery()

        if (transposedChord != 0){
            changeChords()
        }

        return view
    }

    override fun onStop() {
        deleteFrag()
        super.onStop()
    }

    private fun deleteFrag() {
        if (delete) {
            MySQLiteHelper(this.requireContext()).apply {
                deleteFragment(myId, TITLE_TABLE)
            }
        }
    }

    private fun dataRecovery() {
        MySQLiteHelper(this.requireContext()).apply {
            data = searchTitleById(myId)
        }
        binding.title.text = data[0]
        binding.tempo.text = data[1]
        binding.tune.text = data[2]
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun changeChords() {
        transposition = ChordTransposition()
        binding.tune.text = transposition.newChords(
            binding.tune.text.toString(),
            transposedChord,
            myTension
        )
    }
}