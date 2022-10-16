package com.example.colega.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.colega.R
import com.example.colega.databinding.SourceNewsItemBinding
import com.example.colega.models.news.SourceResponseItem

private const val TAG = "SourceNewsAdapter"

class SourceNewsAdapter: RecyclerView.Adapter<SourceNewsAdapter.ViewHolder>() {

    private lateinit var listener: OnItemClickListener

    private var diffCallback = object : DiffUtil.ItemCallback<SourceResponseItem>(){
        override fun areItemsTheSame(
            oldItem: SourceResponseItem,
            newItem: SourceResponseItem
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: SourceResponseItem,
            newItem: SourceResponseItem
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    interface OnItemClickListener{
        fun onItemClick(sourceResponseItem: SourceResponseItem, isClicked: Boolean)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    inner class ViewHolder(val binding: SourceNewsItemBinding):RecyclerView.ViewHolder(binding.root) {
        var isClicked = false
        init {
            binding.btnFollow.setOnClickListener {
                isClicked = !isClicked
                listener.onItemClick(differ.currentList[absoluteAdapterPosition], isClicked)
                if(isClicked){
                    binding.btnFollow.setBackgroundResource(R.drawable.follow_button_selected)
                    binding.btnFollow.text = binding.root.context.getText(R.string.following)
                    binding.btnFollow.setCompoundDrawables(null,null,null,null)
                    binding.btnFollow.setTextColor(binding.root.context.getColor(R.color.white))
                }else{
                    binding.btnFollow.setBackgroundResource(R.drawable.follow_button)
                    binding.btnFollow.text = binding.root.context.getText(R.string.follow)
                    binding.btnFollow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_round_add_24_black,0,0,0)
                    binding.btnFollow.setTextColor(binding.root.context.getColor(R.color.black))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SourceNewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: $position")
        holder.binding.tvNameSource.text = differ.currentList[position].name
        holder.binding.tvDescSource.text = differ.currentList[position].description
        holder.binding.tvCountry.text = differ.currentList[position].country.uppercase()
        holder.binding.tvLanguage.text = differ.currentList[position].language
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun setSourceNewsList(list: List<SourceResponseItem>) {
        Log.d(TAG, "setSourceNewsList: $list")
        differ.submitList(list)
    }
}