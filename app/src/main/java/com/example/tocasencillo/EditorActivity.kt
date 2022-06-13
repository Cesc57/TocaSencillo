package com.example.tocasencillo

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tocasencillo.HomeActivity.Companion.meId
import com.example.tocasencillo.MySQLiteHelper.Companion.ID_DB
import com.example.tocasencillo.MySQLiteHelper.Companion.NAME
import com.example.tocasencillo.MySQLiteHelper.Companion.SONG_TABLE
import com.example.tocasencillo.databinding.ActivityEditorBinding

class EditorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditorBinding

    companion object {
        var posic: Int = 0
        var label: String = "tag"
        var reps: String = "x3"
        var saving = false   //In clickListener from btnSaveSong, this value change to true
        lateinit var songsDBHelper: MySQLiteHelper
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        restartValues()

        songsDBHelper = MySQLiteHelper(this)
        songsDBHelper.readableDatabase

        binding.floatDelete.setOnClickListener {
            // build alert dialog
            val dialogBuilder = android.app.AlertDialog.Builder(this)
            // set message of alert dialog
            dialogBuilder.setMessage(getString(R.string.exit_without_save))
                // if the dialog is cancelable
                .setCancelable(false)
                // positive button text and action
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                // negative button text and action
                .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                    dialog.cancel()
                }

            val alert = dialogBuilder.create()
            alert.show()
        }

        binding.btnSaveSong.setOnClickListener {
            if (binding.etMainTitle.text.toString() == "") {
                showAlert(getString(R.string.nameless_song), getString(R.string.title_your_song))
            } else {
                if (!checkRepeatedSong(binding.etMainTitle.text.toString())) {
                    saving = true
                    saveSong()
                } else {
                    showAlert(getString(R.string.already_exists), getString(R.string.another_name))
                }

            }

        }

        val menuPopup: TextView = binding.btnAddFrag

        menuPopup.setOnClickListener {
            val menuPopupMenu = PopupMenu(this, menuPopup)
            menuPopupMenu.inflate(R.menu.options_menu)

            menuPopupMenu.setOnMenuItemClickListener {

                when (it.title) {

                    getString(R.string.title) -> {
                        loadFragment(fragment = TitleFragment())
                    }

                    getString(R.string.intro) -> {
                        label = "Intro"
                        loadFragment(fragment = LabelFragment())
                    }

                    getString(R.string.verse) -> {
                        label = "Estrofa"
                        loadFragment(fragment = LabelFragment())
                    }

                    getString(R.string.pre_chorus) -> {
                        label = "PreStrb"
                        loadFragment(fragment = LabelFragment())
                    }

                    getString(R.string.chorus) -> {
                        label = "Strb"
                        loadFragment(fragment = LabelFragment())
                    }

                    getString(R.string.bridge) -> {
                        label = "Puente"
                        loadFragment(fragment = LabelFragment())
                    }

                    getString(R.string.solo) -> {
                        label = "Solo"
                        loadFragment(fragment = LabelFragment())
                    }

                    getString(R.string.end) -> {
                        label = "Final"
                        loadFragment(fragment = LabelFragment())
                    }

                    getString(R.string._4_cc) -> {
                        loadFragment(fragment = ContentFragment())
                    }

                    getString(R.string._8_cc) -> {
                        repeat(2) {
                            loadFragment(fragment = ContentFragment())
                        }
                    }

                    getString(R.string._12_cc) -> {
                        repeat(3) {
                            loadFragment(fragment = ContentFragment())
                        }
                    }

                    getString(R.string._16_cc) -> {
                        repeat(4) {
                            loadFragment(fragment = ContentFragment())
                        }
                    }

                    getString(R.string.alternate_ending) -> {
                        loadFragment(fragment = AlternateEndingFragment())
                    }

                    getString(R.string.text) -> {
                        loadFragment(fragment = NoteFragment())
                    }

                    getString(R.string.x3_times) -> {
                        reps = "x3"
                        loadFragment(fragment = RepeatFragment())
                    }

                    getString(R.string.x4_times) -> {
                        reps = "x4"
                        loadFragment(fragment = RepeatFragment())
                    }

                    getString(R.string.box) -> {
                        loadFragment(fragment = BoxRepeatFragment())
                    }

                    else -> {

                    }
                }
                true
            }
            menuPopupMenu.show()
        }

    }

    private fun restartValues() {
        posic = 0
        saving = false
    }

    private fun showAlert(title: String, content: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(content)
        builder.setPositiveButton("OK", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    @SuppressLint("Recycle")
    private fun checkRepeatedSong(songName: String): Boolean {
        var repeatedSong = false
        val db = songsDBHelper.readableDatabase

        val cursor: Cursor = db.rawQuery(
            "SELECT $NAME FROM $SONG_TABLE ORDER BY $ID_DB",
            null
        )

        cursor.moveToFirst()

        while (!cursor.isAfterLast) {
            val songCursor: String = cursor.getString(0)

            if (songCursor == songName) {
                repeatedSong = true
            }
            cursor.moveToNext()
        }
        return repeatedSong
    }

    private fun saveSong() {
        //Try-catch in case the DB fails
        try {
            songsDBHelper.saveSong(binding.etMainTitle.text.toString(), meId)
            for (fragment in supportFragmentManager.fragments) {
                supportFragmentManager.beginTransaction().remove(fragment!!).commit()
            }
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            posic = 0
            finish()
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.try_save), Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.contFrag, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        posic++
    }

    override fun onBackPressed() {
        if (supportFragmentManager.fragments.isEmpty()) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        super.onBackPressed()
    }
}