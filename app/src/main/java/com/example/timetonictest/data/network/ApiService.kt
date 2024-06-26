package com.example.timetonictest.data.network

import com.example.timetonictest.data.model.CreateAppKeyResponse
import com.example.timetonictest.data.model.CreateOauthKeyResponse
import com.example.timetonictest.data.model.CreateSessKeyResponse
import com.example.timetonictest.data.model.GetAllBooksResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("/live/api.php")
    suspend fun createAppKey(
        @Field("version") version: String,
        @Field("req") req: String,
        @Field("appname") appname: String
    ): Response<CreateAppKeyResponse>

    @FormUrlEncoded
    @POST("/live/api.php")
    suspend fun createOauthKey(
        @Field("version") version: String,
        @Field("req") req: String,
        @Field("login") login: String,
        @Field("pwd") password: String,
        @Field("appkey") appKey: String
    ): Response<CreateOauthKeyResponse>

    @FormUrlEncoded
    @POST("/live/api.php")
    suspend fun createSessKey(
        @Field("version") version: String,
        @Field("req") req: String,
        @Field("o_u") o_u: String,
        @Field("u_c") u_c: String,
        @Field("oauthkey") oauthkey: String
    ): Response<CreateSessKeyResponse>

    @POST("/live/api.php")
    @FormUrlEncoded
    suspend fun getAllBooks(
        @Field("version") version: String,
        @Field("req") req: String,
        @Field("o_u") o_u: String,
        @Field("u_c") u_c: String,
        @Field("sesskey") sesskey: String
    ): Response<GetAllBooksResponse>
}