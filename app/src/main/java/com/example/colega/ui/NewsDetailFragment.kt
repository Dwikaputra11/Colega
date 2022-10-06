package com.example.colega.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.colega.R
import com.example.colega.databinding.FragmentNewsDetailBinding
import com.example.colega.models.Article
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NewsDetailFragment(private val news: Article) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentNewsDetailBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private val TAG = "NewsDetailFragment"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setViews()
        setBehaviour(view)
        binding.btnToUrlPage.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(news.url))
            requireActivity().startActivity(browserIntent)
        }
    }

    private fun setBehaviour(view: View){
        bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)
        bottomSheetBehavior.peekHeight = BottomSheetBehavior.PEEK_HEIGHT_AUTO
        showView(binding.appBarDetail,getActionBarSize())

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                    Log.d(TAG, "onStateChanged: Expanded")
                    showView(binding.appBarDetail, getActionBarSize());
                    hideView(binding.clDetail);
                }
                if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    Log.d(TAG, "onStateChanged: Collapsed")
                    hideView(binding.appBarDetail);
                    showView(binding.clDetail, getActionBarSize());
                }

                if (BottomSheetBehavior.STATE_HIDDEN == newState) {
                    Log.d(TAG, "onStateChanged: Hidden")
                    dismiss();
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })

        // close the bottom sheet
        binding.tvClose.setOnClickListener {
            dismiss()
        }
    }

    private fun setViews(){
        binding.tvDetailCategory.text = "tech"
        binding.tvDetailDesc.text = news.description
        binding.tvDetailContent.text = if(news.content != null) news.content.substringBefore("[") else ""
        binding.tvDetailTitle.text = news.title
        binding.tvDetailDate.text = news.publishedAt
        binding.tvDetailSource.text = news.source.name
        Glide.with(requireContext())
            .load(news.urlToImage)
            .placeholder(R.drawable.news)
            .into(binding.ivNews)
    }

    private fun hideView(view: View) {
        val params = view.layoutParams
        params.height = 0
        view.layoutParams = params
    }

    private fun showView(view: View, size: Int) {
        val params = view.layoutParams
        params.height = size
        view.layoutParams = params
    }

    private fun getActionBarSize(): Int {
        val styledAttributes =
            requireContext().theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
        return styledAttributes.getDimension(0, 0f).toInt()
    }
}