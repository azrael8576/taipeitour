package com.alex.cathaybk_recruit_android.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alex.cathaybk_recruit_android.R
import com.alex.cathaybk_recruit_android.vo.Attraction

/**
 * A simple adapter implementation that shows travel.taipei attractions.
 */
class AttractionAdapter
    : PagingDataAdapter<Attraction, AttractionAdapter.AttractionViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttractionViewHolder {
        return AttractionViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: AttractionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AttractionViewHolder(view: View)
        : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.title)
        private var attraction : Attraction? = null
        init {
            view.setOnClickListener {
                attraction?.url?.let { url ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    view.context.startActivity(intent)
                }
            }
        }

        fun bind(attraction: Attraction?) {
            this.attraction = attraction
            title.text = attraction?.name?: "loading"
        }

        companion object {
            fun create(parent: ViewGroup): AttractionViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.attraction_item, parent, false)
                return AttractionViewHolder(view)
            }
        }
    }

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<Attraction>() {
            override fun areContentsTheSame(oldItem: Attraction, newItem: Attraction): Boolean =
                    oldItem == newItem

            override fun areItemsTheSame(oldItem: Attraction, newItem: Attraction): Boolean =
                    oldItem.id == newItem.id
        }
    }
}
