package com.example.tocasencillo

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences.Editor
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tocasencillo.MySQLiteHelper.Companion.ID_DB
import com.example.tocasencillo.MySQLiteHelper.Companion.SONG_TABLE
import com.example.tocasencillo.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth

enum class ProviderType {
    BASIC,
    //GOOGLE
}

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var songsDBHelper: MySQLiteHelper
    companion object{
        private lateinit var db: SQLiteDatabase
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        songsDBHelper = MySQLiteHelper(this)

        fillRecyclerView()


//VIDEO SEARCHVIEW:
        //https://www.youtube.com/watch?v=oE8nZRJ9vxA&ab_channel=Foxandroid

//VIDEO RECYCLER VIEW:
        //https://www.youtube.com/watch?v=TKA01fXqzjg&list=PL0bfr51v6JJEh1xtggpg57wN6m5Us3cb1&index=54&ab_channel=NachoCabanes


        binding.svSong.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.mySearch.clearFocus()
                binding.mySearch.text = query
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })

        binding.btnSong.setOnClickListener {
            goEditor()
        }

        binding.btnExit.setOnClickListener {
            //Delete data from sharPref
            val sharePrefs: Editor =
                getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
//OJO: Per eliminar sols alguna sharePref:
            //sharePrefs.remove("mail")
            //sharePrefs.remove("provider")
            sharePrefs.clear()
            sharePrefs.apply()

            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }

    }

    private fun fillRecyclerView() {
        db = songsDBHelper.readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM $SONG_TABLE ORDER BY $ID_DB",
            null)

        val adapter = RecyclerViewAdapterSongs()
        adapter.recyclerViewAdapterSongs(this, cursor)

        binding.recyclerSongs.setHasFixedSize(true)
        binding.recyclerSongs.layoutManager = LinearLayoutManager(this)
        binding.recyclerSongs.adapter = adapter
    }

    private fun goEditor() {
        val intent = Intent(this, EditorActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        // build alert dialog
        val dialogBuilder = AlertDialog.Builder(this)
        // set title of alert dialog
        dialogBuilder.setTitle("SALIR")
            // if the dialog is cancelable
            .setCancelable(false)
            // set message of alert dialog
            .setMessage("¿Estás seguro?")
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