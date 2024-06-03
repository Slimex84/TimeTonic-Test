package com.example.timetonictest.ui.view.landing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.timetonictest.R
import com.example.timetonictest.data.model.Book

// Adapter class for displaying a list of books in a RecyclerView.
class BooksAdapter : ListAdapter<Book, BooksAdapter.BookViewHolder>(BookDiffCallback()) {

    // Creates and returns a new BookViewHolder, inflating the item view layout.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    // Binds the book data to the ViewHolder for the specified position.
    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = getItem(position)
        holder.bind(book)
    }

    // ViewHolder class for holding and binding the views for each book item.
    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.textViewBookName)
        private val coverImageView: ImageView = itemView.findViewById(R.id.imageViewBookCover)

        fun bind(book: Book) {
            titleTextView.text = book.ownerPrefs.title
            val coverUrl = "https://timetonic.com${book.ownerPrefs.oCoverImg}"
            Glide.with(itemView.context)
                .load(coverUrl)
                .into(coverImageView)
        }
    }
}

// DiffUtil.ItemCallback implementation for calculating the difference between two Book items.
class BookDiffCallback : DiffUtil.ItemCallback<Book>() {
    // Checks if two items represent the same book.
    override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem.b_c == newItem.b_c
    }

    // Checks if the content of two items is the same.
    override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem == newItem
    }
}