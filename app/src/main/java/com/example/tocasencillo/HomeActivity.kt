package com.example.tocasencillo

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences.Editor
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tocasencillo.MySQLiteHelper.Companion.ID_DB
import com.example.tocasencillo.MySQLiteHelper.Companion.NAME
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

    companion object {
        private lateinit var db: SQLiteDatabase
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.title = name
        setSupportActionBar(toolbar)

        songsDBHelper = MySQLiteHelper(this)

        fillRecyclerView()

        binding.svSong.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                customSearchRecyclerView(query.toString())
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query.toString().length < 3) {
                    fillRecyclerView()
                } else {
                    customSearchRecyclerView(query.toString())
                }
                return false
            }

        })

        binding.floatSong.setOnClickListener {
            goEditor()
        }

    }

    private fun clearSignAuth() {
        //Delete data from sharPref
        val sharePrefs: Editor =
            getSharedPreferences(getString(R.string.prefs_file), MODE_PRIVATE).edit()
        //Delete some sharePref:
        sharePrefs.remove("name")
        sharePrefs.remove("mail")
        sharePrefs.remove("provider")
        //sharePrefs.clear()
        sharePrefs.apply()

        FirebaseAuth.getInstance().signOut()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when {
            item.toString() == "Opciones" -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
            item.toString() == "Cerrar sesión" -> {
                onBackPressed()
            }
            else -> {
                Toast.makeText(this, "¿Que has pulsado...?", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

    private fun fillRecyclerView() {
        db = songsDBHelper.readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM $SONG_TABLE ORDER BY $ID_DB",
            null
        )

        val adapter = RecyclerViewAdapterSongs()
        adapter.recyclerViewAdapterSongs(this, cursor)

        binding.recyclerSongs.setHasFixedSize(true)
        binding.recyclerSongs.layoutManager = LinearLayoutManager(this)
        binding.recyclerSongs.adapter = adapter
    }

    private fun customSearchRecyclerView(query: String) {
        db = songsDBHelper.readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM $SONG_TABLE WHERE $NAME LIKE '%$query%' ORDER BY $ID_DB",
            null
        )

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
                clearSignAuth()
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

    override fun onResume() {

        val sharePrefs =
            getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        binding.svSong.setQuery("", false)
        binding.svSong.clearFocus()
        fillRecyclerView()
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.title = sharePrefs.getString("name", null)

        super.onResume()
    }

}