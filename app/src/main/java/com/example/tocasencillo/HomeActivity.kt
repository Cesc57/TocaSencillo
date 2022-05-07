package com.example.tocasencillo

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences.Editor
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tocasencillo.MySQLiteHelper.Companion.ID_DB
import com.example.tocasencillo.MySQLiteHelper.Companion.ID_USER
import com.example.tocasencillo.MySQLiteHelper.Companion.NAME
import com.example.tocasencillo.MySQLiteHelper.Companion.SONG_TABLE
import com.example.tocasencillo.MySQLiteHelper.Companion.USER_TABLE
import com.example.tocasencillo.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth

enum class ProviderType {
    BASIC,
    //GOOGLE
}

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var songsDBHelper: MySQLiteHelper
    private var orderBy: String = "_id"
    private var order: String = "ASC"
    private var isRegistered: Boolean = false
    private lateinit var db: SQLiteDatabase

    companion object {
        var meId: Int = 0
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

        saveUserAndGetUserId()
        fillRecyclerView()

        val spinner = binding.spOrder
        val adapter: ArrayAdapter<*> = ArrayAdapter.createFromResource(
            this, R.array.order_by, android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_item
        )
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {

                when (parent.getItemAtPosition(position).toString()) {
                    "Fecha Ascendente" -> {
                        orderBy = ID_DB
                        order = "ASC"
                        rebuildRecycler()
                    }
                    "Fecha Descendente" -> {
                        orderBy = ID_DB
                        order = "DESC"
                        rebuildRecycler()
                    }
                    "Nombre Ascendente" -> {
                        orderBy = NAME
                        order = "ASC"
                        rebuildRecycler()
                    }
                    "Nombre Descendente" -> {
                        orderBy = NAME
                        order = "DESC"
                        rebuildRecycler()
                    }
                    else -> {
                        Toast.makeText(this@HomeActivity, "Error al ordenar", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

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

    @SuppressLint("Recycle")
    private fun saveUserAndGetUserId() {
        val sharePrefs = getSharedPreferences(getString(R.string.prefs_file), MODE_PRIVATE)
        val mail: String? = sharePrefs.getString("mail", null)

        db = songsDBHelper.readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM $USER_TABLE",
            null
        )

        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(1) == mail.toString()) {
                    isRegistered = true
                }
            } while (cursor.moveToNext())
        }

        meId = if (isRegistered) {
            songsDBHelper.searchUserIdByMail(mail.toString())
        } else {
            songsDBHelper.saveUser(mail.toString())
            songsDBHelper.searchUserIdByMail(mail.toString())
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
            "SELECT * " +
                    "FROM $SONG_TABLE " +
                    "WHERE $SONG_TABLE.$ID_USER = $meId " +
                    "ORDER BY $orderBy $order",
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
            "SELECT * " +
                    "FROM $SONG_TABLE " +
                    "WHERE $NAME LIKE '%$query%' " +
                    "AND $SONG_TABLE.$ID_USER = $meId " +
                    "ORDER BY $orderBy $order",
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
        rebuildRecycler()
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.title = sharePrefs.getString("name", null)

        super.onResume()
    }

    private fun rebuildRecycler() {
        binding.svSong.setQuery("", false)
        binding.svSong.clearFocus()
        fillRecyclerView()
    }
}