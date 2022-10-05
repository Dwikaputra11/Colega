package com.example.colega.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.colega.R
import com.example.colega.data.OnBoarding
import com.example.colega.databinding.OnBoardingItemBinding

class OnBoardingAdapter(private val onBoardingItems: ArrayList<OnBoarding>):
    RecyclerView.Adapter<OnBoardingAdapter.ViewHolder>() {
    private lateinit var context: Context
    class ViewHolder(val binding: OnBoardingItemBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = OnBoardingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(position == onBoardingItems.size - 1) holder.binding.btnNext.visibility = View.VISIBLE
        Glide.with(context)
            .load(onBoardingItems[position].img)
            .placeholder(R.drawable.ic_on_boarding_1)
            .into(holder.binding.ivOnBoard)
        holder.binding.tvOnBoardTitle.text = onBoardingItems[position].title
        holder.binding.tvOnBoardDesc.text = onBoardingItems[position].desc
        holder.binding.btnNext.setOnClickListener {
            Navigation.findNavController(holder.binding.root).navigate(R.id.action_onBoardingFragment_to_loginFragment)
        }
    }

    override fun getItemCount(): Int {
        return onBoardingItems.size
    }
}