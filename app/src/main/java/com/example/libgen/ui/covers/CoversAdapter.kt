package com.example.libgen.ui.covers

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.libgen.R
import kotlinx.android.synthetic.main.layout_book_item.view.*

class CoversAdapter(private val glide: RequestManager) :
    ListAdapter<Uri, CoversAdapter.ViewHolder>(StringDC()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_cover_item, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    fun swapData(data: List<Uri>) = submitList(data.toMutableList())

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(uri: Uri) = with(itemView) {

            glide.load(uri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img_cover)

            Unit
        }
    }


    private class StringDC : DiffUtil.ItemCallback<Uri>() {
        override fun areItemsTheSame(
            oldItem: Uri,
            newItem: Uri
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: Uri,
            newItem: Uri
        ): Boolean = oldItem == newItem

    }
}
