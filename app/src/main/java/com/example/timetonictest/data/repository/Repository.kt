package com.example.timetonictest.data.repository

import com.example.timetonictest.data.model.GetAllBooksResponse
import retrofit2.Response
import com.example.timetonictest.data.model.CreateAppKeyResponse
import com.example.timetonictest.data.model.CreateOauthKeyResponse
import com.example.timetonictest.data.model.CreateSessKeyResponse
import com.example.timetonictest.data.network.ApiService
import retrofit2.Call

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
        return apiService.createOauthKey(version, req, login, password, appkey)
    }

    suspend fun createSessKey(
        version: String,
        req: String,
        o_u: String,
        u_c: String,
        oauthkey: String
    ): Response<CreateSessKeyResponse> {
        return apiService.createSessKey(version, req, o_u, u_c, oauthkey)
    }


    suspend fun fetchBooks(sesskey: String, u_c: String): Response<GetAllBooksResponse> {
        return apiService.getAllBooks(
            version = "6.49q/6.49",
            req = "getAllBooks",
            o_u = u_c,
            u_c = u_c,
            sesskey = sesskey
        )
    }
}


//class Repository(private val apiService: ApiService) {
//    suspend fun createAppKey(
//        version: String,
//        req: String,
//        appname: String
//    ): Response<CreateAppKeyResponse> {
//        return apiService.createAppKey(version, req, appname).execute()
//    }
//
//    suspend fun createOauthKey(
//        version: String,
//        req: String,
//        login: String,
//        password: String,
//        appKey: String
//    ): Response<CreateOauthKeyResponse> {
//        return apiService.createOauthKey(version, req, login, password, appKey).execute()
//    }
//
//    suspend fun createSesskey(
//        version: String,
//        req: String,
//        o_u: String,
//        u_c: String,
//        oauthkey: String
//    ): Response<CreateSessKeyResponse> {
//        return apiService.createSessKey(version, req, o_u, u_c, oauthkey).execute()
//    }
//
//    suspend fun fetchBooks(sesskey: String, u_c: String): Response<GetAllBooksResponse> {
//        val parameters = mapOf(
//            "version" to "6.49q/6.49",
//            "req" to "getAllBooks",
//            "o_u" to u_c,
//            "u_c" to u_c,
//            "sesskey" to sesskey
//        )
//        return apiService.getAllBooks(parameters)
//    }
//}