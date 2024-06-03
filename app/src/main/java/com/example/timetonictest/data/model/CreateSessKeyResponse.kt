package com.example.timetonictest.data.model

data class CreateSessKeyResponse(
    val status: String,
    val sesskey: String?,
    val errorCode: String?,
    val errorMsg: String?
)