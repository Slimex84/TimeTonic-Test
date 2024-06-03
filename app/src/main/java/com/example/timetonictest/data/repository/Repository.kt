package com.example.timetonictest.data.repository

import android.util.Log
import com.example.timetonictest.data.model.GetAllBooksResponse
import retrofit2.Response
import com.example.timetonictest.data.model.CreateAppKeyResponse
import com.example.timetonictest.data.model.CreateOauthKeyResponse
import com.example.timetonictest.data.model.CreateSessKeyResponse
import com.example.timetonictest.data.network.ApiService

class Repository(private val apiService: ApiService) {
    suspend fun createAppKey(
        version: String,
        req: String,
        appname: String
    ): Response<CreateAppKeyResponse> {
        return apiService.createAppKey(version, req, appname)
    }

    suspend fun createOauthKey(
        version: String,
        req: String,
        login: String,
        password: String,
        appkey: String
    ): Response<CreateOauthKeyResponse> {
        val cleanLogin = login.trim()  // Clean the value (remove spaces)
        return apiService.createOauthKey(version, req, cleanLogin, password, appkey)
    }

    suspend fun createSessKey(
        version: String,
        req: String,
        o_u: String,
        u_c: String,
        oauthkey: String
    ): Response<CreateSessKeyResponse> {
        val clean_o_u = o_u.trim()  // Clean the value (remove spaces)
        val clean_u_c = u_c.trim()  // Clean the value (remove spaces)
        return apiService.createSessKey(version, req, clean_o_u, clean_u_c, oauthkey)
    }

    suspend fun fetchBooks(
        version: String,
        req: String,
        o_u: String,
        u_c: String,
        sesskey: String
    ): Response<GetAllBooksResponse> {
        Log.d(
            "Repository",
            "Fetching books with version: $version, o_u: $o_u, u_c: $u_c, sesskey: $sesskey"
        )
        return apiService.getAllBooks(version, req, o_u, u_c, sesskey)
    }
}