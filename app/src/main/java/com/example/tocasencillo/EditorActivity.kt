package com.example.tocasencillo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tocasencillo.databinding.ActivityEditorBinding
import java.lang.Exception

class EditorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditorBinding

    companion object {
        var posic: Int = 0
        var label: String = "tag"
        var saving = false   //In clickListener from btnSaveSong, this value change to true
        lateinit var songsDBHelper: MySQLiteHelper

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        songsDBHelper = MySQLiteHelper(this)
        songsDBHelper.readableDatabase

        binding.floatDelete.setOnClickListener {
            songsDBHelper.close()
            finish()
        }

        binding.btnSaveSong.setOnClickListener {
            if (binding.etMainTitle.text.toString() == "") {
                Toast.makeText(this, "Pon nombre a tu canción", Toast.LENGTH_SHORT).show()
            } else {
                saving = true
                saveSong()
            }

        }

        val menuPopup: TextView = binding.btnAddFrag

        menuPopup.setOnClickListener {
            val menuPopupMenu = PopupMenu(this, menuPopup)
            menuPopupMenu.inflate(R.menu.options_menu)

            menuPopupMenu.setOnMenuItemClickListener {

                when (it.title) {

                    "Titulo" -> {
                        loadFragment(fragment = TitleFragment())
                    }

                    "Intro" -> {
                        label = "Intro"
                        loadFragment(fragment = LabelFragment())
                    }

                    "Estrofa" -> {
                        label = "Estrofa"
                        loadFragment(fragment = LabelFragment())
                    }

                    "PreEstribillo" -> {
                        label = "PreStrb"
                        loadFragment(fragment = LabelFragment())
                    }

                    "Estribillo" -> {
                        label = "Strb"
                        loadFragment(fragment = LabelFragment())
                    }

                    "Puente" -> {
                        label = "Puente"
                        loadFragment(fragment = LabelFragment())
                    }

                    "Solo" -> {
                        label = "Solo"
                        loadFragment(fragment = LabelFragment())
                    }

                    "Final" -> {
                        label = "Final"
                        loadFragment(fragment = LabelFragment())
                    }

                    "4 cc" -> {
                        loadFragment(fragment = ContentFragment())
                    }

                    "8 cc" -> {
                        repeat(2) {
                            loadFragment(fragment = ContentFragment())
                        }
                    }

                    "12 cc" -> {
                        repeat(3) {
                            loadFragment(fragment = ContentFragment())
                        }
                    }

                    "16 cc" -> {
                        repeat(4) {
                            loadFragment(fragment = ContentFragment())
                        }
                    }

                    "Texto" -> {
                        loadFragment(fragment = NoteFragment())
                    }

                    else -> {

                    }
                }
                true
            }
            menuPopupMenu.show()
        }

    }

    private fun saveSong() {
        //Try-catch in case the DB fails
        try {
            songsDBHelper.saveSong(binding.etMainTitle.text.toString())
            for (fragment in supportFragmentManager.fragments) {
                supportFragmentManager.beginTransaction().remove(fragment!!).commit()
            }
            songsDBHelper.close()
            finish()
        } catch (e: Exception) {
            Toast.makeText(this, "ERROR, prueba otra vez a guardar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val fragmentIntercambio = supportFragmentManager.beginTransaction()
        fragmentIntercambio.add(R.id.contFrag, fragment)
        fragmentIntercambio.addToBackStack(null)
        fragmentIntercambio.commit()
        posic++
    }

}