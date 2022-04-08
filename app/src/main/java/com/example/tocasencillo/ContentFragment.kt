package com.example.tocasencillo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.tocasencillo.EditorActivity.Companion.posic
import com.example.tocasencillo.EditorActivity.Companion.saving
import com.example.tocasencillo.databinding.FragmentContentBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ContentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ContentFragment : Fragment() {
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

    private var _binding: FragmentContentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val myPosic = posic

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentContentBinding.inflate(inflater, container, false)
        val view = binding.root

        /*the first and the last chord bar
         can be changed to repeat bars*/
        val bar1: TextView = binding.tvLine1
        bar1.setOnClickListener {
            when (bar1.text) {
                "|" -> {
                    bar1.text = "|:"
                }
                else -> bar1.text = "|"
            }
        }

        val bar5: TextView = binding.tvLine5
        bar5.setOnClickListener {
            when (bar5.text) {
                "|" -> {
                    bar5.text = ":|"
                    //bar5.autoSizeMaxTextSize()
                }
                else -> bar5.text = "|"
            }
        }

        return view
    }

    override fun onStop() {
        byeFragContent()
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun byeFragContent() {
        if (saving) {
            val ccBar1: String = binding.tvLine1.text.toString()
            val ccBar5: String = binding.tvLine5.text.toString()
            val txtCC1: String = binding.etBar1.text.toString()
            val txtCC2: String = binding.etBar2.text.toString()
            val txtCC3: String = binding.etBar3.text.toString()
            val txtCC4: String = binding.etBar4.text.toString()
            MySQLiteHelper(this.requireContext()).apply {
                saveContent(ccBar1, ccBar5, txtCC1, txtCC2, txtCC3, txtCC4)
                saveSongFragment(lastSong(),
                    lastFragment("contenido"),
                    "contenido",
                    myPosic)
            }
        } else {
            posic--
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ContentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ContentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}