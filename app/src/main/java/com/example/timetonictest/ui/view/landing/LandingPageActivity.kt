package com.example.timetonictest.ui.view.landing;

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timetonictest.R
import com.tuapp.viewmodel.LandingPageViewModel

/**
 * Activity class representing the landing page of the application.
 * This activity fetches and displays a list of books using a RecyclerView.
 */
class LandingPageActivity : AppCompatActivity() {

    // ViewModel instance for managing UI-related data in a lifecycle-conscious way.
    private val landingPageViewModel: LandingPageViewModel by viewModels()
    private lateinit var booksAdapter: BooksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

        // Retrieve session key and username from the intent extras.
        val sesskey = intent.getStringExtra("sesskey")
        val username = intent.getStringExtra("username")

        // Set up the RecyclerView with the adapter.
        setupRecyclerView()

        // Fetch and display books if session key and username are valid.
        if (sesskey != null && username != null) {
            landingPageViewModel.getAllBooks("6.49q/6.49", username, username, sesskey)
            landingPageViewModel.booksResponse.observe(this, Observer { response ->
                if (response.isSuccessful) {
                    // Filter out "contacts" books and update the adapter's list.
                    val books = response.body()?.allBooks?.books?.filter { it.b_c != "contacts" }
                    booksAdapter.submitList(books)
                } else {
                    // Handle error case and log the error message.
                    val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                    showError("Failed to fetch books: $errorMsg")
                    Log.e(
                        "LandingPageActivity",
                        "Failed to fetch books: ${response.code()} - $errorMsg"
                    )
                }
            })
        } else {
            // Show error message if session data is invalid.
            showError("Invalid session data")
        }
    }

    // Sets up the RecyclerView with a BooksAdapter and a LinearLayoutManager.
    private fun setupRecyclerView() {
        booksAdapter = BooksAdapter()
        findViewById<RecyclerView>(R.id.recyclerViewBooks).apply {
            adapter = booksAdapter
            layoutManager = LinearLayoutManager(this@LandingPageActivity)
        }
    }

    // Displays an error message using a Toast.
    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}