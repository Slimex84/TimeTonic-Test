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
                    loginViewModel.createOauthKey("6.49q/6.49", "createOauthkey", login, password, appKey)
                    Toast.makeText(this, "Get AppKey Successful", Toast.LENGTH_SHORT).show()
                } else {
                    showError("Failed to get appKey")
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                showError("Failed to create appKey: $errorMessage")
                Log.e("LoginActivity", "Failed to create appKey: ${response.code()} - $errorMessage")
                Log.e("LoginActivity", "Full response: ${response.raw().toString()}")
            }
        })

        // Observers for the LiveData from ViewModel
        loginViewModel.oauthKeyResponse.observe(this, Observer { response ->
            Log.d("LoginActivity", "OAuthKey Response: ${response.raw().toString()}")
            if (response.isSuccessful) {
                val oauthKeyResponse = response.body()
                if (oauthKeyResponse  != null) {
                    val login = editTextEmail.text.toString()
                    val oauthKey = oauthKeyResponse.oauthkey
                    Log.d("LoginActivity", "Creating Sesskey with oauthKey: $oauthKey, o_u: $login")
                    if (oauthKey != null) {
                        loginViewModel.createSesskey("6.49q/6.49", "createSesskey", login, login, oauthKey)
                    }
                    Toast.makeText(this, "Get Oauthkey Successful", Toast.LENGTH_SHORT).show()
                } else {
                    showError("Failed to get oauthKey")
                }
            } else {
                showError("Failed to create oauthKey")
            }
        })

        // Observador para la respuesta de SessKey
        loginViewModel.sessKeyResponse.observe(this, Observer { response ->
            Log.d("LoginActivity", "SessKey Response: ${response.raw().toString()}")
            if (response.isSuccessful) {
                val sesskeyResponse = response.body()
                if (sesskeyResponse != null) {
                    // Maneja la creación exitosa de la sesskey
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    goToLandingPage()
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

    private fun goToLandingPage() {
        val intent = Intent(this, LandingPageActivity::class.java)
        startActivity(intent)
    }
}


//class LoginActivity : AppCompatActivity() {
//
////    // Proporciona una instancia de Repository aquí
////    private val repository: Repository = Repository()
//    private lateinit var binding: ActivityLoginBinding
//    private val loginViewModel: LoginViewModel by viewModels()
//
////    // Usa LoginViewModelFactory para obtener el ViewModel
////    private val loginViewModel: LoginViewModel by viewModels {
////        LoginViewModelFactory(repository)
////    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityLoginBinding.inflate(layoutInflater)
//        enableEdgeToEdge()
//        setContentView(binding.root)
//
//        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
//        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
//        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
//
//        buttonLogin.setOnClickListener {
//            val login = editTextEmail.text.toString()
//            val password = editTextPassword.text.toString()
//            if (login.isNotBlank() && password.isNotBlank()) {
//                loginViewModel.createAppKey("6.49q/6.49", "createAppKey", "TimeTonicTest")
//            } else {
//                showError("Please enter your login and password")
//            }
//        }
//
//        // Observers for the LiveData from ViewModel
//        loginViewModel.appKeyResponse.observe(this, Observer { response ->
//            if (response.isSuccessful) {
//                val appKey = response.body()?.appkey
//                if (appKey != null) {
//                    val login = editTextEmail.text.toString()
//                    val password = editTextPassword.text.toString()
//                    loginViewModel.createOauthKey("6.49q/6.49", "createOauthKey", login, password, appKey)
//                } else {
//                    showError("Failed to get appKey")
//                }
//            } else {
//                showError("Failed to create appKey")
//            }
//        })
//
//        loginViewModel.oauthKeyResponse.observe(this, Observer { response ->
//            if (response.isSuccessful) {
//                val oauthKey = response.body()?.oauthkey
//                if (oauthKey != null) {
//                    // Replace "your_o_u" and "your_u_c" with the actual values
//                    loginViewModel.createSesskey("6.49q/6.49", "createSesskey", "your_o_u", "your_u_c", oauthKey)
//                } else {
//                    showError("Failed to get oauthKey")
//                }
//            } else {
//                showError("Failed to create oauthKey")
//            }
//        })
//
//        loginViewModel.sessKeyResponse.observe(this, Observer { response ->
//            if (response.isSuccessful) {
//                val sesskey = response.body()?.sesskey
//                if (sesskey != null) {
//                    // Handle successful sesskey creation
//                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
//                } else {
//                    showError("Failed to get sesskey")
//                }
//            } else {
//                showError("Failed to create sesskey")
//            }
//        })
//    }
//
//    private fun showError(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//    }
//}