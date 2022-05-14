package com.example.tocasencillo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tocasencillo.AssemblyActivity.Companion.delete
import com.example.tocasencillo.AssemblyActivity.Companion.id_song_frag
import com.example.tocasencillo.MySQLiteHelper.Companion.CONTENT_TABLE
import com.example.tocasencillo.databinding.FragmentContentBuildBinding

class ContentBuildFragment : Fragment() {

    private val myId: Int = id_song_frag
    private lateinit var data: ArrayList<String>
    private lateinit var transpositor: ChordTranspositor

    private var _binding: FragmentContentBuildBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContentBuildBinding.inflate(inflater, container, false)

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
                deleteFragment(myId, CONTENT_TABLE)
            }
        }
    }

    private fun dataRecovery() {
        MySQLiteHelper(this.requireContext()).apply {
            data = searchContentById(myId)
        }
        binding.tvLine1.text = data[0]
        binding.tvLine5.text = data[1]
        binding.tvBar1.text = data[2]
        binding.tvBar2.text = data[3]
        binding.tvBar3.text = data[4]
        binding.tvBar4.text = data[5]
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun changeChords() {
        transpositor = ChordTranspositor()
        val text = transpositor.newChords()
        Toast.makeText(this.requireContext(), text, Toast.LENGTH_SHORT).show()
    }
}