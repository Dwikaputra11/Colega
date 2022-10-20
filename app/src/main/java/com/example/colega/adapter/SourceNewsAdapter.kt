package com.example.colega.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.colega.R
import com.example.colega.data.source.Source
import com.example.colega.databinding.SourceNewsItemBinding
import com.example.colega.models.news.SourceResponseItem

private const val TAG = "SourceNewsAdapter"

class SourceNewsAdapter: RecyclerView.Adapter<SourceNewsAdapter.ViewHolder>() {

    private lateinit var listener: OnItemClickListener

    private var diffCallback = object : DiffUtil.ItemCallback<Source>(){
        override fun areItemsTheSame(
            oldItem: Source,
            newItem: Source
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: Source,
            newItem: Source
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    interface OnItemClickListener{
        fun onItemClick(source: Source)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    inner class ViewHolder(val binding: SourceNewsItemBinding):RecyclerView.ViewHolder(binding.root) {
        init {
            binding.btnFollow.setOnClickListener {
                listener.onItemClick(differ.currentList[absoluteAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SourceNewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvNameSource.text = differ.currentList[position].name
        holder.binding.tvDescSource.text = differ.currentList[position].description
        holder.binding.tvCountry.text = differ.currentList[position].country.uppercase()
        holder.binding.tvLanguage.text = differ.currentList[position].language
        if(differ.currentList[position].isFollow){
            holder.binding.btnFollow.setBackgroundResource(R.drawable.follow_button_selected)
            holder.binding.btnFollow.text =  holder.binding.root.context.getText(R.string.following)
            holder.binding.btnFollow.setCompoundDrawables(null,null,null,null)
            holder.binding.btnFollow.setTextColor( holder.binding.root.context.getColor(R.color.white))
        }else{
            holder.binding.btnFollow.setBackgroundResource(R.drawable.follow_button)
            holder.binding.btnFollow.text =  holder.binding.root.context.getText(R.string.follow)
            holder.binding.btnFollow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_round_add_24_black,0,0,0)
            holder.binding.btnFollow.setTextColor( holder.binding.root.context.getColor(R.color.black))
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun setSourceNewsList(list: List<Source>) {
        differ.submitList(list)
    }
}