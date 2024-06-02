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

class BooksAdapter(books: Any) : ListAdapter<Book, BooksAdapter.BookViewHolder>(BookDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = getItem(position)
        holder.bind(book)
    }

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.textViewBookName)
        private val coverImageView: ImageView = itemView.findViewById(R.id.imageViewBookCover)

        fun bind(book: Book) {
            titleTextView.text = book.b_c
            val coverUrl = "https://timetonic.com/live/dbi/in/tb/${book.b_o}/${book.b_c}/${book.ownerPrefs.oCoverImg}"
            Glide.with(itemView.context)
                .load(coverUrl)
                .into(coverImageView)
        }
    }
}

class BookDiffCallback : DiffUtil.ItemCallback<Book>() {
    override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem.b_c == newItem.b_c
    }

    override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem == newItem
    }
}