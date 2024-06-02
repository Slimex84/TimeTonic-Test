//package com.example.timetonictest.ui.viewmodel
//
//import android.util.Log
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.timetonictest.data.model.CreateAppKeyResponse
//import com.example.timetonictest.data.model.CreateOauthKeyResponse
//import com.example.timetonictest.data.model.CreateSessKeyResponse
//import com.example.timetonictest.data.repository.Repository
//import kotlinx.coroutines.launch
//import okhttp3.ResponseBody
//import retrofit2.Response
//
//class LoginViewModel(private val repository: Repository) : ViewModel() {
//
//    private val _sessionToken = MutableLiveData<String?>()
//    val sessionToken: MutableLiveData<String?> = _sessionToken
//
//    private val _appKeyResponse = MutableLiveData<Response<CreateAppKeyResponse>>()
//    val appKeyResponse: LiveData<Response<CreateAppKeyResponse>> get() = _appKeyResponse
//
//    private val _oauthKeyResponse = MutableLiveData<Response<CreateOauthKeyResponse>>()
//    val oauthKeyResponse: LiveData<Response<CreateOauthKeyResponse>> get() = _oauthKeyResponse
//
//    private val _sessKeyResponse = MutableLiveData<Response<CreateSessKeyResponse>>()
//    val sessKeyResponse: LiveData<Response<CreateSessKeyResponse>> get() = _sessKeyResponse
//
//    fun createAppKey(version: String, req: String, appname: String) {
//        viewModelScope.launch {
//            try {
//                val response = repository.createAppKey(version, req, appname)
//                if (response.isSuccessful) {
//                    _appKeyResponse.postValue(response)
//                } else {
//                    Log.e("LoginViewModel", "Error response: ${response.raw().toString()}")
//                    _appKeyResponse.postValue(response.errorBody()
//                        ?.let { Response.error(response.code(), it) })
//                }
//            } catch (e: Exception) {
//                Log.e("LoginViewModel", "Exception: ${e.message}", e)
//                _appKeyResponse.postValue(Response.error(500, ResponseBody.create(null, "Error: ${e.message}")))
//            }
//        }
//    }
//
//    fun createOauthKey(version: String, req: String, login: String, password: String, appKey: String) {
//        viewModelScope.launch {
//            val response = repository.createOauthKey(version, req, login, password, appKey)
//            _oauthKeyResponse.postValue(response)
//        }
//    }
//
//    fun createSesskey(version: String, req: String, o_u: String, u_c: String, oauthkey: String) {
//        viewModelScope.launch {
//            val response = repository.createSessKey(version, req, o_u, u_c, oauthkey)
//            _sessKeyResponse.postValue(response)
//        }
//    }
//}



//package com.example.timetonictest.ui.viewmodel
//
//import android.util.Log
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.timetonictest.data.model.CreateAppKeyResponse
//import com.example.timetonictest.data.model.CreateOauthKeyResponse
//import com.example.timetonictest.data.model.CreateSessKeyResponse
//import com.example.timetonictest.data.repository.Repository
//import kotlinx.coroutines.launch
//import okhttp3.ResponseBody
//import retrofit2.Response
//
//class LoginViewModel(private val repository: Repository) : ViewModel() {
//
//    private val _sessionToken = MutableLiveData<String?>()
//    val sessionToken: MutableLiveData<String?> = _sessionToken
//
//    private val _appKeyResponse = MutableLiveData<Response<CreateAppKeyResponse>>()
//    val appKeyResponse: LiveData<Response<CreateAppKeyResponse>> get() = _appKeyResponse
//
//    private val _oauthKeyResponse = MutableLiveData<Response<CreateOauthKeyResponse>>()
//    val oauthKeyResponse: LiveData<Response<CreateOauthKeyResponse>> get() = _oauthKeyResponse
//
//    private val _sessKeyResponse = MutableLiveData<Response<CreateSessKeyResponse>>()
//    val sessKeyResponse: LiveData<Response<CreateSessKeyResponse>> get() = _sessKeyResponse
//
//    fun createAppKey(version: String, req: String, appname: String) {
//        viewModelScope.launch {
//            try {
//                val response = repository.createAppKey(version, req, appname)
//                if (response.isSuccessful) {
//                    _appKeyResponse.postValue(response)
//                } else {
//                    Log.e("LoginViewModel", "Error response: ${response.errorBody()?.string()}")
//                    _appKeyResponse.postValue(response)
//                }
//            } catch (e: Exception) {
//                Log.e("LoginViewModel", "Exception: ${e.message}", e)
//                _appKeyResponse.postValue(Response.error(500, ResponseBody.create(null, "Error: ${e.message}")))
//            }
//        }
//    }
//
//    fun createOauthKey(version: String, req: String, login: String, password: String, appKey: String) {
//        viewModelScope.launch {
//            try {
//                val response = repository.createOauthKey(version, req, login, password, appKey)
//                if (response.isSuccessful) {
//                    _oauthKeyResponse.postValue(response)
//                } else {
//                    Log.e("LoginViewModel", "Error response: ${response.errorBody()?.string()}")
//                    _oauthKeyResponse.postValue(response)
//                }
//            } catch (e: Exception) {
//                Log.e("LoginViewModel", "Exception: ${e.message}", e)
//                _oauthKeyResponse.postValue(Response.error(500, ResponseBody.create(null, "Error: ${e.message}")))
//            }
//        }
//    }
//
//    fun createSesskey(version: String, req: String, o_u: String, u_c: String, oauthkey: String) {
//        viewModelScope.launch {
//            try {
//                val response = repository.createSessKey(version, req, o_u, u_c, oauthkey)
//                if (response.isSuccessful) {
//                    _sessKeyResponse.postValue(response)
//                } else {
//                    Log.e("LoginViewModel", "Error response: ${response.errorBody()?.string()}")
//                    _sessKeyResponse.postValue(response)
//                }
//            } catch (e: Exception) {
//                Log.e("LoginViewModel", "Exception: ${e.message}", e)
//                _sessKeyResponse.postValue(Response.error(500, ResponseBody.create(null, "Error: ${e.message}")))
//            }
//        }
//    }
//}


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
                _appKeyResponse.postValue(Response.error(500, ResponseBody.create(null, "Error: ${e.message}")))
            }
        }
    }

    fun createOauthKey(version: String, req: String, login: String, password: String, appKey: String) {
        viewModelScope.launch {
            try {
                val response = repository.createOauthKey(version, req, login, password, appKey)
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

    // Función para crear SessKey en ViewModel
    fun createSesskey(version: String, req: String, o_u: String, u_c: String, oauthkey: String) {
        viewModelScope.launch {
            try {
                Log.d("LoginViewModel", "Sending request: version=$version, req=$req, o_u=$o_u, u_c=$u_c, oauthkey=$oauthkey")
                val response = repository.createSessKey(version, req, o_u, u_c, oauthkey)
                if (response.isSuccessful) {
                    _sessKeyResponse.postValue(response)
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("LoginViewModel", "Error response: $errorBody")
                    _sessKeyResponse.postValue(response)
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Exception: ${e.message}", e)
                _sessKeyResponse.postValue(Response.error(500, ResponseBody.create(null, "Error: ${e.message}")))
            }
        }
    }
}