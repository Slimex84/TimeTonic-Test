package com.example.timetonictest.ui.view.login

import android.annotation.SuppressLint
import android.content.Context
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

/**
 * Activity class for handling user login.
 * This main activity manages user authentication and navigation to the landing page upon successful login.
 */
class LoginActivity : AppCompatActivity() {

    // Instance of ApiService for network operations.
    private val apiService = Service.create()

    // Repository instance for data operations.
    private val repository: Repository = Repository(apiService)

    // View binding for this activity.
    private lateinit var binding: ActivityLoginBinding

    // Username to be used in the session.
    private var username: String = ""

    // saveSessionAppKey and saveSessionOauthKey to be used on shared preferences.
    private var saveSessionAppKey: String = ""
    private var saveSessionOauthKey: String = ""

    // ViewModel instance with a factory for creating the ViewModel with dependencies.
    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout using view binding.
        binding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Initialize login components.
        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)

        // Set an OnClickListener for the login button.
        buttonLogin.setOnClickListener {
            val login = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            if (login.isNotBlank() && password.isNotBlank()) {
                // Request to create AppKey with the provided login and password.
                loginViewModel.createAppKey("6.49q/6.49", "createAppkey", "TimeTonicTest")
            } else {
                // Show error if login or password is empty.
                showError("Please enter your login and password")
            }
        }

        // Observer for the AppKey response from the ViewModel.
        loginViewModel.appKeyResponse.observe(this, Observer { response ->
            if (response.isSuccessful) {
                val appKeyResponse = response.body()
                if (appKeyResponse != null) {
                    // Request to create OAuthKey using the retrieved AppKey.
                    val login = editTextEmail.text.toString()
                    val password = editTextPassword.text.toString()
                    val appkey = appKeyResponse?.appkey
                    if (appkey != null) {
                        loginViewModel.createOauthKey(
                            "6.49q/6.49",
                            "createOauthkey",
                            login,
                            password,
                            appkey
                        )
                    }
                    saveSessionAppKey = appKeyResponse.appkey.toString()
                    Toast.makeText(this, "Get AppKey Successful", Toast.LENGTH_SHORT).show()
                } else {
                    showError("Failed to get appKey")
                }
            } else {
                // Handle error case and log the error message.
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                showError("Failed to create appKey: $errorMessage")
                Log.e(
                    "LoginActivity",
                    "Failed to create appKey: ${response.code()} - $errorMessage"
                )
                Log.e("LoginActivity", "Full response: ${response.raw().toString()}")
            }
        })

        // Observer for the OAuthKey response.
        loginViewModel.oauthKeyResponse.observe(this, Observer { response ->
            if (response.isSuccessful) {
                val oauthKeyResponse = response.body()
                if (oauthKeyResponse?.status == "ok") {
                    val oauthKey = oauthKeyResponse.oauthkey
                    val o_u = oauthKeyResponse.o_u  // Obtain o_u from the response.
                    val u_c = o_u  // Use the same value for u_c.
                    if (oauthKey != null && o_u != null && u_c != null) {
                        // Request to create Sesskey using the retrieved OAuthKey.
                        loginViewModel.createSesskey(
                            "6.49q/6.49",
                            "createSesskey",
                            o_u,
                            u_c,
                            oauthKey
                        )
                        // Set the o_u response to the variable username.
                        username = oauthKeyResponse.o_u
                        saveSessionOauthKey = oauthKeyResponse.oauthkey
                        Toast.makeText(this, "Get Oauthkey Successful", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    showError("Failed to get oauthKey")
                }
            } else {
                showError("Failed to create oauthKey")
            }
        })

        // Observer for the Sesskey response.
        loginViewModel.sessKeyResponse.observe(this, Observer { response ->
            Log.d("LoginActivity", "SessKey Response: ${response.raw().toString()}")
            if (response.isSuccessful) {
                val sesskeyResponse = response.body()
                if (sesskeyResponse?.status == "ok" && sesskeyResponse.sesskey != null) {
                    val sesskey = sesskeyResponse.sesskey
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    saveSessionData(username, saveSessionAppKey, saveSessionOauthKey, sesskey)
                    // Navigate to the landing page with the retrieved Sesskey and username.
                    goToLandingPage(sesskey, username)
                } else {
                    showError("Failed to get sesskey: ${sesskeyResponse?.errorMsg}")
                }
            } else {
                // Handle error case and log the error message.
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                showError("Failed to create sesskey: $errorMessage")
                Log.e(
                    "LoginActivity",
                    "Failed to create sesskey: ${response.code()} - $errorMessage"
                )
                Log.e("LoginActivity", "Full response: ${response.raw().toString()}")
            }
        })
    }

    // Override the onStart function to perform actions when the activity starts.
    override fun onStart() {
        super.onStart()
        checkSession() // Check if there is an active session.
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Navigates to the landing page activity with the provided session key and username.
    private fun goToLandingPage(sesskey: String, username: String) {
        val intent = Intent(this, LandingPageActivity::class.java)
        intent.putExtra("sesskey", sesskey)
        intent.putExtra("username", username)
        startActivity(intent)
    }

    // Control of the back button.
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val startHomeScreen = Intent(Intent.ACTION_MAIN)
        startHomeScreen.addCategory(Intent.CATEGORY_HOME)
        startHomeScreen.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(startHomeScreen)
    }

    // Function to save session data in SharedPreferences.
    private fun saveSessionData(
        username: String,
        appKey: String,
        oauthKey: String,
        sesskey: String
    ) {
        val sharedPreferences = getSharedPreferences("SessionPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("username", username)
            putString("appKey", appKey)
            putString("oauthKey", oauthKey)
            putString("sesskey", sesskey)
            apply()
        }
    }

    // Function to check if a user session exists.
    private fun checkSession() {
        // Access shared preferences to retrieve stored session data.
        val sharedPreferences = getSharedPreferences("SessionPrefs", Context.MODE_PRIVATE)
        val sesskey = sharedPreferences.getString("sesskey", null)
        val username = sharedPreferences.getString("username", null)

        // If session key and username exist, navigate to the landing page.
        if (sesskey != null && username != null) {
            goToLandingPage(sesskey, username)
        }
    }
}