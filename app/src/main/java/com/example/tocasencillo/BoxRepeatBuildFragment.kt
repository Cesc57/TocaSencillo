package com.example.tocasencillo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tocasencillo.AssemblyActivity.Companion.delete
import com.example.tocasencillo.AssemblyActivity.Companion.id_song_frag
import com.example.tocasencillo.MySQLiteHelper.Companion.BOX_REPEAT_TABLE
import com.example.tocasencillo.databinding.FragmentBoxRepeatBuildBinding

class BoxRepeatBuildFragment : Fragment() {

    private val myPosic: Int = id_song_frag

    private var _binding: FragmentBoxRepeatBuildBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBoxRepeatBuildBinding.inflate(inflater, container, false)
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
                deleteFragment(myPosic, BOX_REPEAT_TABLE)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun dataRecovery() {

        MySQLiteHelper(this.requireContext()).apply {
            binding.rptText.text = searchBoxRepeatById(myPosic)
        }

    }
}