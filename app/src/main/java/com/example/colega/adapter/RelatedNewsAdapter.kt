package com.example.colega.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.colega.R
import com.example.colega.data.RelatedNews
import com.example.colega.databinding.RelatedNewsItemBinding

class RelatedNewsAdapter(private var relatedNews: List<RelatedNews>):
    RecyclerView.Adapter<RelatedNewsAdapter.ViewHolder>() {
    class ViewHolder(val binding: RelatedNewsItemBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RelatedNewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.binding.root.context)
            .load(relatedNews[position].img)
            .placeholder(R.drawable.news)
            .into(holder.binding.ivRelated)
        holder.binding.tvRelatedCategory.text = relatedNews[position].category
        holder.binding.tvRelatedSource.text = relatedNews[position].source
        holder.binding.tvRelatedTitle.text = relatedNews[position].title
        holder.binding.tvRelatedTime.text = relatedNews[position].date.time.toString()
    }

    override fun getItemCount(): Int {
        return relatedNews.size
    }

    fun setRelatedNews(list: List<RelatedNews>){
        this.relatedNews = list
    }

}