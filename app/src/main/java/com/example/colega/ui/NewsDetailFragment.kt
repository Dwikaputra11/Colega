package com.example.colega.ui

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.colega.R
import com.example.colega.data.users.Bookmark
import com.example.colega.databinding.FragmentNewsDetailBinding
import com.example.colega.models.news.ArticleResponse
import com.example.colega.utils.UtilMethods
import com.example.colega.viewmodel.BookmarkViewModel
import com.example.colega.viewmodel.UserViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NewsDetailFragment(private val news: ArticleResponse?, private val bookmark: Bookmark?) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentNewsDetailBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var bookmarkVM: BookmarkViewModel
    private lateinit var userVM: UserViewModel
    private var userId = -1
    private val TAG = "NewsDetailFragment"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bookmarkVM = ViewModelProvider(this)[BookmarkViewModel::class.java]
        userVM = ViewModelProvider(this)[UserViewModel::class.java]
        userVM.dataUser.observe(viewLifecycleOwner){
            userId = it.userId
        }
        setViews()
        setBehaviour(view)
        binding.btnToUrlPage.setOnClickListener {
            if (news != null){
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(news.url))
                requireActivity().startActivity(browserIntent)
            }
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
        if(news != null){
            binding.tvDetailCategory.text = "tech"
            binding.tvDetailDesc.text = news.description
            binding.tvDetailContent.text = if(news.content != null) news.content.substringBefore("[") else ""
            binding.tvDetailTitle.text = news.title
            binding.tvDetailDate.text = UtilMethods.convertISOTime(requireContext(), news.publishedAt)
            binding.tvDetailSource.text = news.articleSource.name
            Glide.with(requireContext())
                .load(news.urlToImage)
                .placeholder(R.drawable.news)
                .into(binding.ivNews)
        }else if(bookmark != null){
            binding.tvDetailCategory.text = "tech"
            binding.tvDetailDesc.text = bookmark.description
            binding.tvDetailContent.text = if(bookmark.content != null) bookmark.content.substringBefore("[") else ""
            binding.tvDetailTitle.text = bookmark.title
            binding.tvDetailDate.text = UtilMethods.convertISOTime(requireContext(), bookmark.publishedAt)
            binding.tvDetailSource.text = bookmark.source
            Glide.with(requireContext())
                .load(bookmark.urlToImage)
                .placeholder(R.drawable.news)
                .into(binding.ivNews)
//            if(bookmark.isCheck){
//                binding.btnBookmark.isChecked = true
//            }
            binding.btnBookmark.isChecked = true
        }
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

    override fun onDismiss(dialog: DialogInterface) {
        Log.d(TAG, "onDismiss: ${binding.btnBookmark.isChecked}")
        if(binding.btnBookmark.isChecked) {
            if(news != null){
                if (userId != -1) {
                    Log.d(TAG, "onDismiss: $userId")
                    val bookmark = Bookmark(
                        userId = userId,
                        author = news.author,
                        publishedAt = news.publishedAt,
                        urlToImage = news.urlToImage,
                        description = news.description,
                        content = news.content,
                        source = news.articleSource.name,
                        title = news.title,
                        url = news.url,
                        id = 0,
//                        isCheck = true,
                    )
                    bookmarkVM.insertBookmark(bookmark)
                    bookmarkVM.postBookmarkToApi(bookmark)
                    Log.d(TAG, "onDismiss: Done")
                }
            }
        }else{
            if(news == null && bookmark != null){
                if (userId != -1) {
                    Log.d(TAG, "onDismiss: $userId")
                    bookmarkVM.deleteBookmark(bookmark)
                    bookmarkVM.deleteBookmarkUserFromApi(userId.toString(), bookmark.id.toString())
                    Log.d(TAG, "onDismiss: Done")
                }
            }
        }
        super.onDismiss(dialog)
    }
}