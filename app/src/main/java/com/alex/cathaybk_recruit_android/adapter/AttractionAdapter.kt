package com.alex.cathaybk_recruit_android.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alex.cathaybk_recruit_android.AttractionListFragmentDirections
import com.alex.cathaybk_recruit_android.OnClickAttractionListener
import com.alex.cathaybk_recruit_android.databinding.AttractionItemBinding
import com.alex.cathaybk_recruit_android.vo.Attraction

fun actionToAttractionDetailFragment(attraction: Attraction?, onClickAttractionListener: OnClickAttractionListener): View.OnClickListener {
    return View.OnClickListener { view ->
        if (attraction == null) return@OnClickListener

        onClickAttractionListener.setClickedAttraction(attraction)
        val direction =
            AttractionListFragmentDirections.actionAttractionListFragmentToAttractionDetailFragment()
        view.findNavController().navigate(direction)
    }
}

/**
 * A simple adapter implementation that shows travel.taipei attractions.
 */
class AttractionAdapter(onClickAttractionListener: OnClickAttractionListener)
    : PagingDataAdapter<Attraction, AttractionAdapter.AttractionViewHolder>(COMPARATOR) {

    private var mOnClickAttractionListener: OnClickAttractionListener

    init {
        mOnClickAttractionListener = onClickAttractionListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttractionViewHolder {
        return AttractionViewHolder(
            AttractionItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            mOnClickAttractionListener
        )
    }

    override fun onBindViewHolder(holder: AttractionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AttractionViewHolder(private val binding: AttractionItemBinding, private val onClickAttractionListener: OnClickAttractionListener)
        : RecyclerView.ViewHolder(binding.root) {
        private var attraction : Attraction? = null

        fun bind(attraction: Attraction?) {
            this.attraction = attraction

            binding.title.text = attraction?.name?: "loading"
            binding.root.setOnClickListener(actionToAttractionDetailFragment(attraction, onClickAttractionListener))
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
