package com.example.tocasencillo

import android.annotation.SuppressLint
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

        restore("")

        binding.floatName.setOnClickListener {
            val prefs: SharedPreferences.Editor =
                getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.putString("name", binding.etName.text.toString())
            prefs.apply()
            restore("name")
        }

        binding.floatInstrument.setOnClickListener {
            val prefs: SharedPreferences.Editor =
                getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.putString("instrument", binding.etInstrument.text.toString())
            prefs.apply()
            restore("instrument")
        }

        binding.btnPassword.setOnClickListener {
            if (binding.etNewPass.text.toString() == binding.etConfirmNewPass.text.toString()) {
                Toast.makeText(this, "Encara no funciona...", Toast.LENGTH_SHORT).show()
            }else {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            }
        }

        //FirebaseAuth.getInstance().currentUser?.updatePassword(binding.etNewPass.text.toString())
        //FirebaseAuth.getInstance().currentUser?.updatePassword("pepe")

    }

    @SuppressLint("SetTextI18n")
    private fun restore(edited: String) {
        when {
            edited === "name" -> {
                val sharePrefs =
                    getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
                val name: String? = sharePrefs.getString("name", null)
                binding.tvName.text = "Nombre: $name"
                binding.etName.text.clear()
            }
            edited === "instrument" -> {
                val sharePrefs =
                    getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
                val instrument: String? = sharePrefs.getString("instrument", null)
                binding.tvInstrument.text = "Instrumento: $instrument"
                binding.etInstrument.text.clear()
            }
            else -> {
                val sharePrefs =
                    getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
                val name: String? = sharePrefs.getString("name", null)
                val instrument: String? = sharePrefs.getString("instrument", null)
                binding.tvName.text = "Nombre: $name"
                binding.tvInstrument.text = "Instrumento: $instrument"
                binding.etInstrument.text.clear()
            }
        }


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