package com.example.colega.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.colega.R
import com.example.colega.data.article.RelatedNews
import com.example.colega.databinding.RelatedNewsItemBinding
import com.example.colega.models.news.ArticleResponse
import com.example.colega.utils.PaginationAdapterCallback
import com.example.colega.utils.UtilMethods
import com.example.colega.utils.Utils

class RelatedNewsAdapter():
    RecyclerView.Adapter<RelatedNewsAdapter.ViewHolder>(), PaginationAdapterCallback {
    private val TAG = "RelatedNewsAdapter"
    private lateinit var listener: OnItemClickListener
    private val item: Int = 0
    private val loading: Int = 1

    private var isLoadingAdded: Boolean = false
    private var retryPageLoad: Boolean = false

    private var errorMsg: String? = ""

    interface OnItemClickListener{
        fun onItemClick(news: RelatedNews)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    private var diffCallback = object : DiffUtil.ItemCallback<RelatedNews>(){
        override fun areItemsTheSame(oldItem: RelatedNews, newItem: RelatedNews): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: RelatedNews, newItem: RelatedNews): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    inner class ViewHolder(val binding: RelatedNewsItemBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                listener.onItemClick(differ.currentList[absoluteAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RelatedNewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.binding.root.context)
            .load(differ.currentList[position].urlToImage)
            .placeholder(R.drawable.news)
            .into(holder.binding.ivRelated)
        holder.binding.tvRelatedCategory.text = Utils.category_technology
        holder.binding.tvRelatedSource.text = differ.currentList[position].source
        holder.binding.tvRelatedTitle.text = differ.currentList[position].title
        holder.binding.tvRelatedTime.text = UtilMethods.convertISOTime(holder.binding.root.context,differ.currentList[position].publishedAt)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun setRelatedNews(list: List<RelatedNews>){
        differ.submitList(list)
    }

    override fun retryPageLoad() {

    }

}