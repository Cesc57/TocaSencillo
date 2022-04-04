package com.example.tocasencillo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences.*
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tocasencillo.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth

enum class ProviderType {
    BASIC,
    //GOOGLE
}

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var songsDBHelper: MySQLiteHelper
    private lateinit var db: SQLiteDatabase

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
            "SELECT * FROM cancion ORDER BY _id",
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
}