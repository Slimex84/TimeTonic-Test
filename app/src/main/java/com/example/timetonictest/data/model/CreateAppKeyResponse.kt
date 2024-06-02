package com.example.timetonictest.data.model

data class CreateAppKeyResponse(
    val status: String,
    val appkey: String?,
    val errorCode: String?,
    val errorMsg: String?
)