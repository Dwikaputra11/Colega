package com.example.colega.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.colega.R
import com.example.colega.data.News
import com.example.colega.databinding.HeadlineItemBinding

class HeadlineAdapter(private val headlineList: List<News>): RecyclerView.Adapter<HeadlineAdapter.ViewHolder>() {
    class ViewHolder(val binding: HeadlineItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HeadlineItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.root.layoutParams =
            ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.binding.root.context)
            .load(headlineList[position].img)
            .placeholder(R.drawable.news)
            .into(holder.binding.ivHeadline)
        holder.binding.tvHeadlineTitle.text = headlineList[position].title
        holder.binding.tvHeadlineDate.text = headlineList[position].date
    }

    override fun getItemCount(): Int {
        return headlineList.size
    }
}