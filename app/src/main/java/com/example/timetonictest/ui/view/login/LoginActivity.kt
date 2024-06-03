package com.example.timetonictest.ui.view.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.timetonictest.R
import com.example.timetonictest.data.network.Service
import com.example.timetonictest.data.repository.Repository
import com.example.timetonictest.databinding.ActivityLoginBinding
import com.example.timetonictest.ui.view.landing.LandingPageActivity
import com.example.timetonictest.ui.viewmodel.LoginViewModel
import com.example.timetonictest.ui.viewmodel.LoginViewModelFactory


class LoginActivity : AppCompatActivity() {

    // Proporciona una instancia de ApiService aquí
    private val apiService = Service.create()
    private val repository: Repository = Repository(apiService)
    private lateinit var binding: ActivityLoginBinding

    // Usa LoginViewModelFactory para obtener el ViewModel
    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)

        buttonLogin.setOnClickListener {
            val login = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            if (login.isNotBlank() && password.isNotBlank()) {
                loginViewModel.createAppKey("6.49q/6.49", "createAppkey", "TimeTonicTest")
            } else {
                showError("Please enter your login and password")
            }
        }

        // Observers for the LiveData from ViewModel
        loginViewModel.appKeyResponse.observe(this, Observer { response ->
            if (response.isSuccessful) {
                val appKey = response.body()?.appkey
                if (appKey != null) {
                    val login = editTextEmail.text.toString()
                    val password = editTextPassword.text.toString()
                    loginViewModel.createOauthKey(
                        "6.49q/6.49",
                        "createOauthkey",
                        login,
                        password,
                        appKey
                    )
                    Toast.makeText(this, "Get AppKey Successful", Toast.LENGTH_SHORT).show()
                } else {
                    showError("Failed to get appKey")
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                showError("Failed to create appKey: $errorMessage")
                Log.e(
                    "LoginActivity",
                    "Failed to create appKey: ${response.code()} - $errorMessage"
                )
                Log.e("LoginActivity", "Full response: ${response.raw().toString()}")
            }
        })

        // Observador para la respuesta de OAuthKey
        loginViewModel.oauthKeyResponse.observe(this, Observer { response ->
            if (response.isSuccessful) {
                val oauthKeyResponse = response.body()
                if (oauthKeyResponse?.status == "ok") {
                    val oauthKey = oauthKeyResponse.oauthkey
                    val o_u = oauthKeyResponse.o_u  // Obtén o_u de la respuesta
                    val u_c = o_u  // Usa el mismo valor para u_c
                    if (oauthKey != null && o_u != null && u_c != null) {
                        loginViewModel.createSesskey(
                            "6.49q/6.49",
                            "createSesskey",
                            o_u,
                            u_c,
                            oauthKey
                        )
                        Toast.makeText(this, "Get Oauthkey Successful", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    showError("Failed to get oauthKey")
                }
            } else {
                showError("Failed to create oauthKey")
            }
        })

        loginViewModel.sessKeyResponse.observe(this, Observer { response ->
            Log.d("LoginActivity", "SessKey Response: ${response.raw().toString()}")
            if (response.isSuccessful) {
                val sesskeyResponse = response.body()
                if (sesskeyResponse?.status == "ok" && sesskeyResponse.sesskey != null) {
                    val sesskey = sesskeyResponse.sesskey
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    goToLandingPage(sesskey)
                } else {
                    showError("Failed to get sesskey: ${sesskeyResponse?.errorMsg}")
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                showError("Failed to create sesskey: $errorMessage")
                Log.e("LoginActivity", "Failed to create sesskey: ${response.code()} - $errorMessage")
                Log.e("LoginActivity", "Full response: ${response.raw().toString()}")
            }
        })
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Función para navegar a la página de aterrizaje
    private fun goToLandingPage(sesskey: String) {
        val intent = Intent(this, LandingPageActivity::class.java)
        intent.putExtra("sesskey", sesskey)
        startActivity(intent)
    }
}