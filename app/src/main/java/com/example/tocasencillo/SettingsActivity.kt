package com.example.tocasencillo

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.tocasencillo.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        val sharePrefs =
            getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val name: String? = sharePrefs.getString("name", null)

        binding.etName.hint = name.toString()

        binding.floatName.setOnClickListener {
            val prefs: SharedPreferences.Editor =
                getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.putString("name", binding.etName.toString())
            prefs.apply()
        }

        //FirebaseAuth.getInstance().currentUser?.updatePassword("pepe")

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.settings_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when {
            item.toString() == "SALIR" -> {
                finish()
            }
            else -> {
                Toast.makeText(this, "¿Que has pulsado...?", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

}