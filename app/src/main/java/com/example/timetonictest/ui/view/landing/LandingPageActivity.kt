package com.example.timetonictest.ui.view.landing;

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.timetonictest.R
import com.tuapp.viewmodel.LandingPageViewModel

class LandingPageActivity : AppCompatActivity() {

    private val landingPageViewModel: LandingPageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

        val sesskey = intent.getStringExtra("sesskey")
        val u_c = intent.getStringExtra("u_c")

        if (sesskey != null && u_c != null) {
            landingPageViewModel.getAllBooks(sesskey, u_c)
            landingPageViewModel.booksResponse.observe(this, Observer { response ->
                if (response.isSuccessful) {
                    val books = response.body()?.allBooks
                    // Handle the list of books (e.g., display them in a RecyclerView)
                } else {
                    showError("Failed to fetch books")
                }
            })
        } else {
            showError("Invalid session data")
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}