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

class LoginViewModel(private val repository: Repository) : ViewModel() {

    private val _sessionToken = MutableLiveData<String?>()
    val sessionToken: MutableLiveData<String?> = _sessionToken

    private val _appKeyResponse = MutableLiveData<Response<CreateAppKeyResponse>>()
    val appKeyResponse: LiveData<Response<CreateAppKeyResponse>> get() = _appKeyResponse

    private val _oauthKeyResponse = MutableLiveData<Response<CreateOauthKeyResponse>>()
    val oauthKeyResponse: LiveData<Response<CreateOauthKeyResponse>> get() = _oauthKeyResponse

    private val _sessKeyResponse = MutableLiveData<Response<CreateSessKeyResponse>>()
    val sessKeyResponse: LiveData<Response<CreateSessKeyResponse>> get() = _sessKeyResponse

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

    // Función para crear OAuthKey en ViewModel
    fun createOauthKey(
        version: String,
        req: String,
        login: String,
        password: String,
        appKey: String
    ) {
        viewModelScope.launch {
            try {
                val cleanLogin = login.trim()  // Limpiar valor
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

    fun createSesskey(version: String, req: String, o_u: String, u_c: String, oauthkey: String) {
        viewModelScope.launch {
            try {
                val clean_o_u = o_u.trim()  // Limpiar valor
                val clean_u_c = u_c.trim()  // Limpiar valor

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