package com.example.timetonictest.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.timetonictest.data.repository.Repository

/**
 * Factory class for creating instances of LoginViewModel with a specific Repository.
 * This ensures that the ViewModel can be instantiated with the required dependencies.
 */
class LoginViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    // Creates a new instance of the given ViewModel class.
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Check if the modelClass is assignable from LoginViewModel.
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository) as T
        }
        // Throw an exception if the ViewModel class is unknown.
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}