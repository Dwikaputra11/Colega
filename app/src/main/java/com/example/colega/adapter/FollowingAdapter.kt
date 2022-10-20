package com.example.colega.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.colega.data.users.FollowingSource
import com.example.colega.databinding.FollowingItemBinding
import com.example.colega.models.user.UserFollowingSource

class FollowingAdapter: RecyclerView.Adapter<FollowingAdapter.ViewHolder>() {

    private lateinit var listener: OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(source: UserFollowingSource)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    private var diffCallback = object : DiffUtil.ItemCallback<UserFollowingSource>(){
        override fun areItemsTheSame(oldItem: UserFollowingSource, newItem: UserFollowingSource): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserFollowingSource, newItem: UserFollowingSource): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    inner class ViewHolder(val binding: FollowingItemBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                listener.onItemClick(differ.currentList[absoluteAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FollowingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvSourceName.text = differ.currentList[position].name
        holder.binding.tvDescSource.text = differ.currentList[position].description
        holder.binding.tvLanguage.text = differ.currentList[position].language
        holder.binding.tvCountry.text = differ.currentList[position].country
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun setFollowingList(list: List<UserFollowingSource>) = differ.submitList(list)
}