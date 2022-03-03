package com.example.tocasencillo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tocasencillo.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth

enum class ProviderType {
    BASIC,
    //GOOGLE
}

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Setup
        //val bundle: Bundle? = intent.extras
        //val name: String? = bundle?.getString("name")
        //val mail: String? = bundle?.getString("mail")
        //val provider: String? = bundle?.getString("provider")
        //setup()

        //Saving data


        binding.btnSong.setOnClickListener {

            goEditor()

        }

        binding.btnExit.setOnClickListener {

            //Delete data from sharPref
            val sharePrefs: Editor =
                getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            sharePrefs.clear()
            sharePrefs.apply()

            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }

    }

    private fun goEditor() {
        val intent = Intent(this, EditorActivity::class.java)
        startActivity(intent)
    }

}