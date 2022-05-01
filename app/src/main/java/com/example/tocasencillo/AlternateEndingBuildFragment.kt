package com.example.tocasencillo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tocasencillo.AssemblyActivity.Companion.delete
import com.example.tocasencillo.AssemblyActivity.Companion.id_song_frag
import com.example.tocasencillo.MySQLiteHelper.Companion.ALTERNATE_ENDING_TABLE
import com.example.tocasencillo.databinding.FragmentAlternateEndingBuildBinding

class AlternateEndingBuildFragment : Fragment() {

    private val myId: Int = id_song_frag
    private lateinit var data: ArrayList<String>

    private var _binding: FragmentAlternateEndingBuildBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlternateEndingBuildBinding.inflate(inflater, container, false)
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
                deleteFragment(myId, ALTERNATE_ENDING_TABLE)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun dataRecovery() {

        MySQLiteHelper(this.requireContext()).apply {
            data = searchAlternateEndingById(myId)
        }
        binding.tvLine2.text = data[0]
        binding.tvBar1.text = data[1]
        binding.tvBar2.text = data[2]
    }
}