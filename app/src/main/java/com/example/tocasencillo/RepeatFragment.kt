package com.example.tocasencillo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tocasencillo.AssemblyActivity.Companion.delete
import com.example.tocasencillo.AssemblyActivity.Companion.id_song_frag
import com.example.tocasencillo.EditorActivity.Companion.posic
import com.example.tocasencillo.EditorActivity.Companion.reps
import com.example.tocasencillo.EditorActivity.Companion.saving
import com.example.tocasencillo.MySQLiteHelper.Companion.REPEAT_TABLE
import com.example.tocasencillo.databinding.FragmentRepeatBinding

class RepeatFragment : Fragment() {

    private val myValue = reps

    private var _binding: FragmentRepeatBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val myPosic = posic

    private var myId: Int = 0

    private val myTableId: Int = id_song_frag

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRepeatBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.tvTimes.text = myValue

        myId = when (reps) {
            getString(R.string.x3_times) -> {
                1
            }
            else -> {
                2

            }
        }

        return view
    }

    override fun onStop() {
        if (delete) {
            MySQLiteHelper(this.requireContext()).apply {
                deletePrefabs(REPEAT_TABLE, myTableId)
            }
        }
        byeFragRepeatTime()
        super.onStop()
    }

    private fun byeFragRepeatTime() {
        if (!saving) {
            posic--
        } else {
            //val title: String = (select id where = label)
            MySQLiteHelper(this.requireContext()).apply {
                saveSongFragment(
                    lastSong(),
                    REPEAT_TABLE,
                    myPosic
                )
                saveRepeat(lastSongFragment(), myId)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}