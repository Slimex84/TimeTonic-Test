package com.example.timetonictest.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timetonictest.data.model.CreateAppKeyResponse
import com.example.timetonictest.data.model.CreateOauthKeyResponse
import com.example.timetonictest.data.model.CreateSessKeyResponse
import com.example.timetonictest.data.repository.Repository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response

// ViewModel for handling login-related operations.
class LoginViewModel(private val repository: Repository) : ViewModel() {

    // LiveData for storing session token
    private val _sessionToken = MutableLiveData<String?>()
    val sessionToken: MutableLiveData<String?> = _sessionToken

    // LiveData for storing responses from createAppKey requests
    private val _appKeyResponse = MutableLiveData<Response<CreateAppKeyResponse>>()
    val appKeyResponse: LiveData<Response<CreateAppKeyResponse>> get() = _appKeyResponse

    // LiveData for storing responses from createOauthKey requests
    private val _oauthKeyResponse = MutableLiveData<Response<CreateOauthKeyResponse>>()
    val oauthKeyResponse: LiveData<Response<CreateOauthKeyResponse>> get() = _oauthKeyResponse

    // LiveData for storing responses from createSessKey requests
    private val _sessKeyResponse = MutableLiveData<Response<CreateSessKeyResponse>>()
    val sessKeyResponse: LiveData<Response<CreateSessKeyResponse>> get() = _sessKeyResponse

    // Function to create an application key.
    fun createAppKey(version: String, req: String, appname: String) {
        viewModelScope.launch {
            try {
                val response = repository.createAppKey(version, req, appname)
                if (response.isSuccessful) {
                    _appKeyResponse.postValue(response)
                } else {
                    Log.e("LoginViewModel", "Error response: ${response.errorBody()?.string()}")
                    _appKeyResponse.postValue(response)
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Exception: ${e.message}", e)
                _appKeyResponse.postValue(
                    Response.error(
                        500,
                        ResponseBody.create(null, "Error: ${e.message}")
                    )
                )
            }
        }
    }

    // Function to create an OAuth key.
    fun createOauthKey(
        version: String,
        req: String,
        login: String,
        password: String,
        appKey: String
    ) {
        viewModelScope.launch {
            try {
                val cleanLogin = login.trim()  // Clean login value (remove spaces)
                val response = repository.createOauthKey(version, req, cleanLogin, password, appKey)
                if (response.isSuccessful) {
                    _oauthKeyResponse.postValue(response)
                } else {
                    Log.e("LoginViewModel", "Error response: ${response.errorBody()?.string()}")
                    _oauthKeyResponse.postValue(response)
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Exception: ${e.message}", e)
                val errorResponseBody = ResponseBody.create(null, "Error: ${e.message}")
                _oauthKeyResponse.postValue(Response.error(500, errorResponseBody))
            }
        }
    }

    // Function to create a session key.
    fun createSesskey(version: String, req: String, o_u: String, u_c: String, oauthkey: String) {
        viewModelScope.launch {
            try {
                val clean_o_u = o_u.trim()  // Clean o_u value (remove spaces)
                val clean_u_c = u_c.trim()  // Clean u_c value (remove spaces)

                val response =
                    repository.createSessKey(version, req, clean_o_u, clean_u_c, oauthkey)
                if (response.isSuccessful) {
                    _sessKeyResponse.postValue(response)
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("LoginViewModel", "Error response: $errorBody")
                    _sessKeyResponse.postValue(response)
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Exception: ${e.message}", e)
                _sessKeyResponse.postValue(
                    Response.error(
                        500,
                        ResponseBody.create(null, "Error: ${e.message}")
                    )
                )
            }
        }
    }
}