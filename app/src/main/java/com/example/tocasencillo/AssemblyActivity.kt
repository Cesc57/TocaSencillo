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
        private var transposedChord: Int = 0
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
                    id_song_frag = cursor.getString(0).toInt()
                    valueLabel(songsDBHelper.searchLabelValue(cursor.getString(0).toInt()))
                }
                REPEAT_TABLE -> {
                    id_song_frag = cursor.getString(0).toInt()
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

        binding.tvPlusTone.setOnClickListener {
            transposedChord = 2
            transposedFragments()
        }

        binding.tvPlusSemiTone.setOnClickListener {
            transposedChord = 1
            transposedFragments()
        }

        binding.tvLessTone.setOnClickListener {
            transposedChord = -2
            transposedFragments()
        }

        binding.tvLessSemiTone.setOnClickListener {
            transposedChord = -1
            transposedFragments()
        }

    }

    private fun transposedFragments() {

        for (fragment in supportFragmentManager.fragments) {
            //Log.d("helloTag", fragment.javaClass.simpleName)
            if (fragment.javaClass.simpleName == "ContentBuildFragment") {
                val fragmentContent = supportFragmentManager.findFragmentById(fragment.id) as ContentBuildFragment
                fragmentContent.changeChords()
            }else if (fragment.javaClass.simpleName == "AlternateEndingBuildFragment"){
                val fragmentAlternate = supportFragmentManager.findFragmentById(fragment.id) as AlternateEndingBuildFragment
                fragmentAlternate.changeChords()
            }
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

            item.toString() == getString(R.string.exit_camel_case) -> {
                onBackPressed()
            }

            item.toString() == getString(R.string.edit) -> {
                Toast.makeText(this, getString(R.string.func_not_available), Toast.LENGTH_SHORT)
                    .show()
            }
            item.toString() == getString(R.string.delete) -> {
                showAlert()
            }
            else -> {
                Toast.makeText(this, getString(R.string.have_pressed), Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

    private fun showAlert() {
        // build alert dialog
        val dialogBuilder = AlertDialog.Builder(this)
        // set title of alert dialog
        dialogBuilder.setTitle(getString(R.string.delete_song))
            // if the dialog is cancelable
            .setCancelable(false)
            // set message of alert dialog
            .setMessage(getString(R.string.are_you_sure))
            // positive button text and action
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                delete = true
                deleteSong()
                songsDBHelper.deleteSong(songName)
            }
            // negative button text and action
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
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
            Toast.makeText(this, getString(R.string.try_delete), Toast.LENGTH_SHORT).show()
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
                getString(R.string.intro)
            }
            2 -> {
                getString(R.string.verse)
            }
            3 -> {
                getString(R.string.pre_chorus)
            }
            4 -> {
                getString(R.string.chorus)
            }
            5 -> {
                getString(R.string.bridge)
            }
            6 -> {
                getString(R.string.solo)
            }
            7 -> {
                getString(R.string.end)
            }
            else -> {
                getString(R.string.heart)
            }
        }
    }

    private fun valueRepeat(valueTable: Int) {
        reps = when (valueTable) {
            1 -> {
                getString(R.string.x3_times)
            }
            2 -> {
                getString(R.string.x4_times)
            }
            else -> {
                getString(R.string.heart)
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
                Toast.makeText(this, getString(R.string.error_occurred), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
        // build alert dialog
        val dialogBuilder = AlertDialog.Builder(this)
        // set message of alert dialog
        dialogBuilder.setMessage(getString(R.string.sure_exit))
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton(R.string.yes) { _, _ ->
                finish()
                super.onBackPressed()
            }
            // negative button text and action
            .setNegativeButton(R.string.no) { dialog, _ ->
                dialog.cancel()
            }

        val alert = dialogBuilder.create()
        alert.show()
    }
}