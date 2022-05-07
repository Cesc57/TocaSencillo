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
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        restore("")

        auth = FirebaseAuth.getInstance()

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
            if (binding.etMail.text.toString().isNotEmpty()) {
                resetPassword()
                binding.etMail.setText("")
            } else {
                Toast.makeText(this, getString(R.string.enter_mail), Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun resetPassword() {
        auth.setLanguageCode("es")
        auth.sendPasswordResetEmail(binding.etMail.text.toString()).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, getString(R.string.email_sent), Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this, getString(R.string.email_not_sent), Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun restore(edited: String) {
        when {
            edited === "name" -> {
                val sharePrefs =
                    getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
                val name: String? = sharePrefs.getString("name", null)
                binding.tvName.text = "${getString(R.string.name_cap)}: $name"
                binding.etName.text.clear()
            }
            edited === "instrument" -> {
                val sharePrefs =
                    getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
                val instrument: String? = sharePrefs.getString("instrument", null)
                binding.tvInstrument.text = "${getString(R.string.instrument_cap)}: $instrument"
                binding.etInstrument.text.clear()
            }
            else -> {
                val sharePrefs =
                    getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
                val name: String? = sharePrefs.getString("name", null)
                val instrument: String? = sharePrefs.getString("instrument", null)
                binding.tvName.text = "${getString(R.string.name_cap)}: $name"
                binding.etName.text.clear()
                binding.tvInstrument.text = "${getString(R.string.instrument_cap)}: $instrument"
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
            item.toString() == getString(R.string.exit) -> {
                finish()
            }
            else -> {
                Toast.makeText(this, getString(R.string.have_pressed), Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }
}