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

/**
 * ViewModel class for managing the landing page data and logic.
 * This ViewModel is responsible for fetching and providing book data to the UI.
 */
class LandingPageViewModel : ViewModel() {

    // Instance of the Repository for data operations.
    private val repository: Repository = Repository(Service.create())

    // LiveData for holding the response of book data.
    private val _booksResponse = MutableLiveData<Response<GetAllBooksResponse>>()
    val booksResponse: LiveData<Response<GetAllBooksResponse>> = _booksResponse

    /**
     * Fetches all books using the provided parameters.
     * The response is posted to the LiveData which can be observed by the UI.
     */
    fun getAllBooks(version: String, o_u: String, u_c: String, sesskey: String) {
        // Launching a coroutine in the ViewModel scope for network operations.
        viewModelScope.launch {
            val response = repository.fetchBooks(version, "getAllBooks", o_u, u_c, sesskey)
            _booksResponse.postValue(response)
        }
    }
}