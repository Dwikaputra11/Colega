package com.example.colega.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.colega.R
import com.example.colega.data.article.HeadlineNews
import com.example.colega.databinding.HeadlineItemBinding
import com.example.colega.models.news.ArticleResponse
import com.example.colega.utils.UtilMethods

class HeadlineAdapter(): RecyclerView.Adapter<HeadlineAdapter.ViewHolder>() {

    private lateinit var listener: OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(news: HeadlineNews)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    private var diffCallback = object : DiffUtil.ItemCallback<HeadlineNews>(){
        override fun areItemsTheSame(oldItem: HeadlineNews, newItem: HeadlineNews): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: HeadlineNews, newItem: HeadlineNews): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    inner class ViewHolder(val binding: HeadlineItemBinding): RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener {
                 listener.onItemClick(differ.currentList[absoluteAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HeadlineItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.binding.root.context)
            .load(differ.currentList[position].urlToImage)
            .placeholder(R.drawable.news)
            .into(holder.binding.ivHeadline)
        holder.binding.tvHeadlineTitle.text = differ.currentList[position].title
        holder.binding.tvHeadlineDate.text = UtilMethods.convertISOTime(holder.binding.root.context,differ.currentList[position].publishedAt)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun setHeadlineList(list: List<HeadlineNews>){
        differ.submitList(list)
    }
}