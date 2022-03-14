package com.example.tocasencillo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.tocasencillo.EditorActivity.Companion.guardando
import com.example.tocasencillo.EditorActivity.Companion.posic
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContentBinding.inflate(inflater, container, false)
        val view = binding.root

        /*Tanto la primera como la última barrita de los acordes
        se pueden cambiar por barras de repetición:*/
        val bar1: TextView =binding.tvLine1
        bar1.setOnClickListener{
            when (bar1.text) {
                "|" -> {
                    bar1.setText("|:")
                }
                else -> bar1.setText("|")
            }
        }

        val bar5: TextView =binding.tvLine5
        bar5.setOnClickListener{
            when (bar5.text) {
                "|" -> {
                    bar5.setText(":|")
                    //bar5.autoSizeMaxTextSize()
                }
                else -> bar5.setText("|")
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onStop() {
        if (guardando==false){
            posic--
        }else{
            //Go to DBB for save the song
            Log.d("Por HACER", "POR HACER")
        }

        super.onStop()
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