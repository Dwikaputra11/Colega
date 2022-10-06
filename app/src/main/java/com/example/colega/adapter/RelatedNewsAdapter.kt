package com.example.colega.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.colega.R
import com.example.colega.data.News
import com.example.colega.databinding.RelatedNewsItemBinding

class RelatedNewsAdapter(private var relatedNews: List<News>):
    RecyclerView.Adapter<RelatedNewsAdapter.ViewHolder>() {
    private lateinit var listener: OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(news: News)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    inner class ViewHolder(val binding: RelatedNewsItemBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                listener.onItemClick(relatedNews[adapterPosition])
            }
        }
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
        holder.binding.tvRelatedTime.text = relatedNews[position].date.toString()
    }

    override fun getItemCount(): Int {
        return relatedNews.size
    }

    fun setRelatedNews(list: List<News>){
        this.relatedNews = list
    }

}