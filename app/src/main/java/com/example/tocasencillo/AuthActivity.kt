package com.example.tocasencillo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tocasencillo.databinding.ActivityAuthBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Lanzamos eventos personalizados a Google Analytics
        //para consultarlo en nuestra consola de Firebase
        val analytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message", "Integración de Firebase completada")
        analytics.logEvent("InitScreen", bundle)


        //error sobre editText, aunque no m'agrada com respon...
//        val fillName = binding.etName
//        if (fillName.text.toString().isBlank()) {
//            fillName.error = "Introduce tu nombre"
//        }
//
//        val fillMail = binding.etMail
//        if (fillMail.text.toString().isBlank()) {
//            fillMail.error = "Introduce tu mail"
//        }
//
//        val fillPass = binding.etPass
//        if (fillPass.text.toString().isBlank()) {
//            fillPass.error = "Introduce tu contraseña"
//        }

        // Setup
        session()
        setup()

    }

    override fun onStart() {
        super.onStart()
        binding.authLayout.visibility = View.VISIBLE
    }

    private fun session() {

        val prefs =
            getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val name: String? = prefs.getString("name", null)
        val mail: String? = prefs.getString("mail", null)
        val provider: String? = prefs.getString("provider", null)

        if (name != null && mail != null && provider != null) {
            binding.authLayout.visibility = View.INVISIBLE
            showHome(name, mail, ProviderType.valueOf(provider))
        }

    }

    private fun setup() {

        binding.btnRegister.setOnClickListener {
            if (binding.etName.text.isNotEmpty() &&
                binding.etMail.text.isNotEmpty() &&
                binding.etPass.text.isNotEmpty()
            ) {

                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(
                        binding.etMail.text.toString(),
                        binding.etPass.text.toString()).addOnCompleteListener {

                        if (it.isSuccessful) {

                            showHome(binding.etName.toString(),
                                it.result?.user?.email ?: "",
                                ProviderType.BASIC)

                            savePrefs()

                        } else {
                            showAlert("Usuario ya registrado\n" +
                                    "prueba pulsando ENTRAR")
                        }

                    }

            }
        }

        binding.btnEnter.setOnClickListener {
            if (binding.etName.text.isNotEmpty() &&
                binding.etMail.text.isNotEmpty() &&
                binding.etPass.text.isNotEmpty()
            ) {

                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(
                        binding.etMail.text.toString(),
                        binding.etPass.text.toString()).addOnCompleteListener {

                        if (it.isSuccessful) {

                            showHome(binding.etName.toString(),
                                it.result?.user?.email ?: "",
                                ProviderType.BASIC)

                            savePrefs()

                        } else {
                            showAlert("Asegúrate de que la contraseña es correcta " +
                                    "o prueba a registrarte primero")
                        }

                    }

            }
        }
    }

    //Saving session preferences
    private fun savePrefs() {
        val prefs: SharedPreferences.Editor =
            getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("name", binding.etName.toString())
        prefs.putString("mail", binding.etMail.toString())
        prefs.putString("provider", ProviderType.BASIC.toString())
        prefs.apply()
    }

    private fun showAlert(message: String) {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("ERROR")
        builder.setMessage(message)
        builder.setPositiveButton("OK", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

    private fun showHome(name: String, mail: String, provider: ProviderType) {
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            putExtra("name", name)
            putExtra("mail", mail)
            putExtra("provider", provider)
        }
        startActivity(homeIntent)
    }

}