package com.example.tocasencillo

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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

    companion object {
        private lateinit var songsDBHelper: MySQLiteHelper
    }

    @SuppressLint("Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssemblyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val mySong: String = intent.getStringExtra("songName").toString()
        //binding.tvMainTitle.text = mySong

        songsDBHelper = MySQLiteHelper(this)
        songsDBHelper.readableDatabase

        val songName: String = intent.getStringExtra("songName").toString()
        val songId: Int = songsDBHelper.searchSongIdByName(songName)

        binding.tvMainTitle.text = songId.toString()

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
            val type: String = cursor.getString(2)
            rebuildFragment(type)
            cursor.moveToNext()
        }

        //cursor.close()

        //TODO:
        //OK//Search song (for name + id)
        //Search all cancion_fragmento with id_song
        // _id_fragmento, tipo TEXT -> Create Fragment (if (tipo = "tipo"))
        //onCreate Fragment -> select * from "tipoQueSea" and .text in all fields

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