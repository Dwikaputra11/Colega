package com.example.colega.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.colega.R
import com.example.colega.data.Bookmark
import com.example.colega.databinding.ActivityBookmarkBinding
import com.example.colega.databinding.HeadlineItemBinding
import com.example.colega.models.Article

class BookmarkAdapter:RecyclerView.Adapter<BookmarkAdapter.ViewHolder>() {

    private lateinit var listener: OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(news: Bookmark)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    private var diffCallback = object : DiffUtil.ItemCallback<Bookmark>(){
        override fun areItemsTheSame(oldItem: Bookmark, newItem: Bookmark): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Bookmark, newItem: Bookmark): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    inner class ViewHolder(val binding: HeadlineItemBinding): RecyclerView.ViewHolder(binding.root) {
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
        holder.binding.tvHeadlineDate.text = differ.currentList[position].publishedAt
        holder.binding.tvHeadlineTitle.text = differ.currentList[position].title
        Glide.with(holder.binding.root.context)
            .load(differ.currentList[position].urlToImage)
            .placeholder(R.drawable.news)
            .into(holder.binding.ivHeadline)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun setBookmarkList(list: List<Bookmark>){
        differ.submitList(list)
    }
}