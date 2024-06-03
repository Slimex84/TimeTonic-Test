package com.example.timetonictest.ui.view.landing;

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timetonictest.R
import com.tuapp.viewmodel.LandingPageViewModel

class LandingPageActivity : AppCompatActivity() {

    private val landingPageViewModel: LandingPageViewModel by viewModels()
    private lateinit var booksAdapter: BooksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

        val sesskey = intent.getStringExtra("sesskey")
        val u_c = intent.getStringExtra("u_c")

        setupRecyclerView()

        if (sesskey != null && u_c != null) {
            landingPageViewModel.getAllBooks("6.49q/6.49", "getAllBooks", u_c, u_c, sesskey)
            landingPageViewModel.booksResponse.observe(this, Observer { response ->
                if (response.isSuccessful) {
                    val books = response.body()?.allBooks?.filter { it.b_c != "contacts" }
                    booksAdapter.submitList(books)
                } else {
                    showError("Failed to fetch books")
                }
            })
        } else {
            showError("Invalid session data")
        }
    }

    private fun setupRecyclerView() {
        booksAdapter = BooksAdapter()
        findViewById<RecyclerView>(R.id.recyclerViewBooks).apply {
            adapter = booksAdapter
            layoutManager = LinearLayoutManager(this@LandingPageActivity)
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}