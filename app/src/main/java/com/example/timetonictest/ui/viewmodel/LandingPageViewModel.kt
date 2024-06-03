package com.tuapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timetonictest.data.model.GetAllBooksResponse
import com.example.timetonictest.data.network.Service
import com.example.timetonictest.data.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class LandingPageViewModel : ViewModel() {

    private val repository: Repository = Repository(Service.create())

    private val _booksResponse = MutableLiveData<Response<GetAllBooksResponse>>()
    val booksResponse: LiveData<Response<GetAllBooksResponse>> = _booksResponse

    fun getAllBooks(version: String, req: String, o_u: String, u_c: String, sesskey: String) {
        viewModelScope.launch {
            val response = repository.fetchBooks(version, req, o_u, u_c, sesskey)
            _booksResponse.postValue(response)
        }
    }
}