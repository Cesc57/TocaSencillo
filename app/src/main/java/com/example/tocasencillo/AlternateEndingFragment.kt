package com.example.tocasencillo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.tocasencillo.EditorActivity.Companion.posic
import com.example.tocasencillo.MySQLiteHelper.Companion.ALTERNATE_ENDING_TABLE
import com.example.tocasencillo.databinding.FragmentAlternateEndingBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AlternateEndingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AlternateEndingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentAlternateEndingBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val myPosic = posic

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlternateEndingBinding.inflate(inflater, container, false)
        val view = binding.root

        val bar2: TextView = binding.tvLine2
        bar2.setOnClickListener {
            when (bar2.text) {
                "|" -> {
                    bar2.text = ":|"
                }
                else -> bar2.text = "|"
            }
        }

        return view
    }

    override fun onStop() {
        byeFragAlternateEnding()
        super.onStop()
    }

    private fun byeFragAlternateEnding() {
        if (EditorActivity.saving) {
            val ccBar2: String = binding.tvLine2.text.toString()
            val txtCC1: String = binding.etBar1.text.toString()
            val txtCC2: String = binding.etBar2.text.toString()
            MySQLiteHelper(this.requireContext()).apply {
                saveAlternateEnd(ccBar2, txtCC1, txtCC2)
                saveSongFragment(lastSong(),
                    lastFragment(ALTERNATE_ENDING_TABLE),
                    ALTERNATE_ENDING_TABLE,
                    myPosic)
            }
        } else {
            posic--
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AlternateEndingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AlternateEndingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}