package com.wei.traveltaipei.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wei.traveltaipei.GlideRequests
import com.wei.traveltaipei.databinding.AttractionItemBinding
import com.wei.traveltaipei.ui.AttractionListFragmentDirections
import com.wei.traveltaipei.ui.OnClickAttractionListener
import com.wei.traveltaipei.vo.Attraction

fun actionToAttractionDetailFragment(
    attraction: Attraction?,
    onClickAttractionListener: OnClickAttractionListener
): View.OnClickListener {
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
class AttractionAdapter(private val glide: GlideRequests, onClickAttractionListener: OnClickAttractionListener) :
    PagingDataAdapter<Attraction, AttractionAdapter.AttractionViewHolder>(COMPARATOR) {

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
            glide,
            mOnClickAttractionListener
        )
    }

    override fun onBindViewHolder(holder: AttractionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AttractionViewHolder(
        private val binding: AttractionItemBinding,
        private val glide: GlideRequests,
        private val onClickAttractionListener: OnClickAttractionListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        private var attraction: Attraction? = null

        fun bind(attraction: Attraction?) {
            this.attraction = attraction

            if (attraction?.images != null) {
                for (image in attraction.images) {
                    if (image.src.startsWith("http") && image.ext.isNotEmpty()) {
                        binding.thumbnail.visibility = View.VISIBLE
                        glide.load(image.src)
                            .centerCrop()
                            .into(binding.thumbnail)
                        break
                    }
                }
            }

            binding.title.text = attraction?.name ?: "loading"
            binding.title.visibility = if (binding.title.text.isNotEmpty()) View.VISIBLE else View.GONE
            binding.subtitle.text = attraction?.introduction ?: ""
            binding.subtitle.visibility = if (binding.subtitle.text.isNotEmpty()) View.VISIBLE else View.GONE
            binding.openTime.text = attraction?.openTime ?: ""
            binding.openTime.visibility = if (binding.openTime.text.isNotEmpty()) View.VISIBLE else View.GONE

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
