package com.example.libgen.ui.listings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.libgen.R
import com.example.libgen.data.models.Book
import com.example.libgen.utli.extensions.formatDate
import com.example.libgen.utli.extensions.gone
import com.example.libgen.utli.extensions.visible
import kotlinx.android.synthetic.main.layout_book_item.view.*
import java.util.*

class BooksAdapter(private val glide: RequestManager) :
    ListAdapter<Book, BooksAdapter.ViewModel>(BookDC()) {

    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewModel(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_book_item, parent, false)
    )

    override fun onBindViewHolder(holder: ViewModel, position: Int) = holder.bind(getItem(position))

    fun swapData(data: List<Book>) = submitList(data.toMutableList())

    inner class ViewModel(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(book: Book) = with(itemView) {

            txt_name.text = book.name

            txt_author.text = book.author

            txt_price.text = book.price

            txt_doi.text = Date(book.doi).formatDate()

            if (book.covers.isNotEmpty()) {
                img_cover.visible()
                val uri = book.covers.split(",")[0]
                glide.load(uri)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(img_cover)
                setOnClickListener { listener?.onCLick(book._id) }
            } else img_cover.gone()

        }
    }


    private class BookDC : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(
            oldItem: Book,
            newItem: Book
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: Book,
            newItem: Book
        ): Boolean = oldItem == newItem

    }

    interface Listener {
        fun onCLick(_id: String)
    }
}
