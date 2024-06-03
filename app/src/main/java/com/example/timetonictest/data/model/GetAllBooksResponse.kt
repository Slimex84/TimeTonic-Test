package com.example.timetonictest.data.model

data class GetAllBooksResponse(
    val status: String,
    val sstamp: Long,
    val allBooks: List<Book>
)

data class Book(
    val b_c: String,
    val b_o: String,
    val ownerPrefs: OwnerPrefs
)

data class OwnerPrefs(
    val title: String,
    val oCoverImg: String?
)