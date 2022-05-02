package com.example.tocasencillo

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.tocasencillo.EditorActivity.Companion.label
import com.example.tocasencillo.EditorActivity.Companion.reps
import com.example.tocasencillo.EditorActivity.Companion.saving
import com.example.tocasencillo.MySQLiteHelper.Companion.ALTERNATE_ENDING_TABLE
import com.example.tocasencillo.MySQLiteHelper.Companion.BOX_REPEAT_TABLE
import com.example.tocasencillo.MySQLiteHelper.Companion.CONTENT_TABLE
import com.example.tocasencillo.MySQLiteHelper.Companion.ID_SONG
import com.example.tocasencillo.MySQLiteHelper.Companion.LABEL_TABLE
import com.example.tocasencillo.MySQLiteHelper.Companion.NOTE_TABLE
import com.example.tocasencillo.MySQLiteHelper.Companion.REPEAT_TABLE
import com.example.tocasencillo.MySQLiteHelper.Companion.SONG_FRAGMENT_TABLE
import com.example.tocasencillo.MySQLiteHelper.Companion.TITLE_TABLE
import com.example.tocasencillo.databinding.ActivityAssemblyBinding

class AssemblyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAssemblyBinding
    private lateinit var db: SQLiteDatabase
    private var posicNow: Int = 1
    private lateinit var songName: String
    private var songId: Int = 0

    companion object {
        private lateinit var songsDBHelper: MySQLiteHelper
        var id_song_frag: Int = 0
        var delete = false
    }

    @SuppressLint("Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssemblyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        restartValues()

        val name = intent.getStringExtra("songName")
        songName = name!!
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.title = name
        setSupportActionBar(toolbar)

        loadAd()

        songsDBHelper = MySQLiteHelper(this)
        songsDBHelper.readableDatabase

        val songName: String = intent.getStringExtra("songName").toString()
        songId = songsDBHelper.searchSongIdByName(songName)

        binding.tvMainTitle.text = songName

        db = songsDBHelper.readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT * " +
                    "FROM $SONG_FRAGMENT_TABLE " +
                    "WHERE $ID_SONG = '$songId'",
            null
        )

        cursor.moveToFirst()

        while (!cursor.isAfterLast) {
            //LOGICAL FOR BUILD FRAGMENT
            if (posicNow % 8 == 0) {
                loadAd()
            }
            val type: String = cursor.getString(2)
            when (type) {
                LABEL_TABLE -> {
                    valueLabel(songsDBHelper.searchLabelValue(cursor.getString(0).toInt()))
                }
                REPEAT_TABLE -> {
                    valueRepeat(songsDBHelper.searchRepeatValue(cursor.getString(0).toInt()))
                }
                else -> {
                    id_song_frag = cursor.getString(0).toInt()
                }
            }
            rebuildFragment(type)
            cursor.moveToNext()
            posicNow++
        }
    }

    private fun restartValues() {
        saving = false
        delete = false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.assembly_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when {

            item.toString() == "Salir" -> {
                onBackPressed()
            }

            item.toString() == "Editar" -> {
                Toast.makeText(this, "Función no disponible por el momento", Toast.LENGTH_SHORT)
                    .show()
            }
            item.toString() == "Eliminar" -> {
                showAlert()
            }
            else -> {
                Toast.makeText(this, "¿Que has pulsado...?", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

    private fun showAlert() {
        // build alert dialog
        val dialogBuilder = AlertDialog.Builder(this)
        // set title of alert dialog
        dialogBuilder.setTitle("ELIMINAR CANCIÓN")
            // if the dialog is cancelable
            .setCancelable(false)
            // set message of alert dialog
            .setMessage("¿Estás seguro?")
            // positive button text and action
            .setPositiveButton("SI") { _, _ ->
                delete = true
                deleteSong()
                songsDBHelper.deleteSong(songName)
            }
            // negative button text and action
            .setNegativeButton("NO") { dialog, _ ->
                dialog.cancel()
            }

        val alert = dialogBuilder.create()
        alert.show()
    }

    private fun deleteSong() {
        //Try-catch in case the DB fails
        try {
            for (fragment in supportFragmentManager.fragments) {
                supportFragmentManager.beginTransaction().remove(fragment!!).commit()
            }
            songsDBHelper.deleteSongFragment(songId)
            songsDBHelper.deleteSong(songName)
            finish()
        } catch (e: Exception) {
            Toast.makeText(this, "ERROR, prueba otra vez a eliminar", Toast.LENGTH_SHORT).show()
        }
    }


    private fun loadAd() {
        val fragment = AdMobFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.assemblySong, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun valueLabel(valueTable: Int) {
        label = when (valueTable) {
            1 -> {
                "Intro"
            }
            2 -> {
                "Estrofa"
            }
            3 -> {
                "PreStrb"
            }
            4 -> {
                "Strb"
            }
            5 -> {
                "Puente"
            }
            6 -> {
                "Solo"
            }
            7 -> {
                "Final"
            }
            else -> {
                "<3"
            }
        }
    }

    private fun valueRepeat(valueTable: Int) {
        reps = when (valueTable) {
            1 -> {
                "x3"
            }
            2 -> {
                "x4"
            }
            else -> {
                "<3"
            }
        }
    }

    private fun rebuildFragment(type: String) {
        when (type) {
            TITLE_TABLE -> {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                val fragment: Fragment = TitleBuildFragment()
                fragmentTransaction.add(R.id.assemblySong, fragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
            CONTENT_TABLE -> {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                val fragment: Fragment = ContentBuildFragment()
                fragmentTransaction.add(R.id.assemblySong, fragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
            NOTE_TABLE -> {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                val fragment: Fragment = NoteBuildFragment()
                fragmentTransaction.add(R.id.assemblySong, fragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
            LABEL_TABLE -> {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                val fragment: Fragment = LabelFragment()
                fragmentTransaction.add(R.id.assemblySong, fragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
            REPEAT_TABLE -> {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                val fragment: Fragment = RepeatFragment()
                fragmentTransaction.add(R.id.assemblySong, fragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
            BOX_REPEAT_TABLE -> {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                val fragment: Fragment = BoxRepeatBuildFragment()
                fragmentTransaction.add(R.id.assemblySong, fragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
            ALTERNATE_ENDING_TABLE -> {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                val fragment: Fragment = AlternateEndingBuildFragment()
                fragmentTransaction.add(R.id.assemblySong, fragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
            else -> {
                Toast.makeText(this, "Ha surgido un error...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
        // build alert dialog
        val dialogBuilder = AlertDialog.Builder(this)
        // set message of alert dialog
        dialogBuilder.setMessage("¿Seguro que quieres salir?")
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("SI") { _, _ ->
                finish()
                super.onBackPressed()
            }
            // negative button text and action
            .setNegativeButton("NO") { dialog, _ ->
                dialog.cancel()
            }

        val alert = dialogBuilder.create()
        alert.show()
    }
}