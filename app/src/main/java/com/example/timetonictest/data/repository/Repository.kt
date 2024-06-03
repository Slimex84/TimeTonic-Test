package com.example.timetonictest.data.repository

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
        val cleanLogin = login.trim()  // Limpiar valor
        return apiService.createOauthKey(version, req, cleanLogin, password, appkey)
    }

    suspend fun createSessKey(
        version: String,
        req: String,
        o_u: String,
        u_c: String,
        oauthkey: String
    ): Response<CreateSessKeyResponse> {
        val clean_o_u = o_u.trim()  // Limpiar valor
        val clean_u_c = u_c.trim()  // Limpiar valor
        return apiService.createSessKey(version, req, clean_o_u, clean_u_c, oauthkey)
    }

    suspend fun fetchBooks(
        version: String,
        req: String,
        o_u: String,
        u_c: String,
        sesskey: String
    ): Response<GetAllBooksResponse> {
        return apiService.getAllBooks(version, req, o_u, u_c, sesskey)
    }
}