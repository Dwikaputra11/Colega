package com.example.colega.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.colega.R
import com.example.colega.data.Headline
import com.example.colega.databinding.HeadlineItemBinding

class HeadlineAdapter(private val headlineList: List<Headline>): RecyclerView.Adapter<HeadlineAdapter.ViewHolder>() {
    class ViewHolder(val binding: HeadlineItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HeadlineItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.binding.root.context)
            .load(headlineList[position].img)
            .placeholder(R.drawable.news)
            .into(holder.binding.ivHeadline)
        holder.binding.tvHeadlineCategory.text = headlineList[position].category
        holder.binding.tvHeadlineTitle.text = headlineList[position].title
        holder.binding.tvHeadlineDate.text = headlineList[position].date.toString()
    }

    override fun getItemCount(): Int {
        return headlineList.size
    }
}