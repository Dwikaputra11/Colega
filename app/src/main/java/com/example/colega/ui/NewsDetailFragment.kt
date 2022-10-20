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
import com.example.colega.data.article.HeadlineNews
import com.example.colega.data.article.RelatedNews
import com.example.colega.data.users.Bookmark
import com.example.colega.databinding.FragmentNewsDetailBinding
import com.example.colega.models.user.UserBookmark
import com.example.colega.utils.UtilMethods
import com.example.colega.viewmodel.BookmarkViewModel
import com.example.colega.viewmodel.UserViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NewsDetailFragment(
    private val headlineNews: HeadlineNews?,
    private val relatedNews: RelatedNews?,
    private val bookmark: UserBookmark?,
) : BottomSheetDialogFragment() {
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
            if (relatedNews != null){
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(relatedNews.url))
                requireActivity().startActivity(browserIntent)
            }else if(headlineNews != null){
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(headlineNews.url))
                requireActivity().startActivity(browserIntent)
            }else if(bookmark != null){
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(bookmark.url))
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
        if(relatedNews != null){
            binding.tvDetailCategory.text = "tech"
            binding.tvDetailDesc.text = relatedNews.description
            binding.tvDetailContent.text = if(relatedNews.content != null) relatedNews.content.substringBefore("[") else ""
            binding.tvDetailTitle.text = relatedNews.title
            binding.tvDetailDate.text = UtilMethods.convertISOTime(requireContext(), relatedNews.publishedAt)
            binding.tvDetailSource.text = relatedNews.source
            Glide.with(requireContext())
                .load(relatedNews.urlToImage)
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
        }else if(headlineNews != null){
            binding.tvDetailCategory.text = "tech"
            binding.tvDetailDesc.text = headlineNews.description
            binding.tvDetailContent.text = if(headlineNews.content != null) headlineNews.content.substringBefore("[") else ""
            binding.tvDetailTitle.text = headlineNews.title
            binding.tvDetailDate.text = UtilMethods.convertISOTime(requireContext(), headlineNews.publishedAt)
            binding.tvDetailSource.text = headlineNews.source
            Glide.with(requireContext())
                .load(headlineNews.urlToImage)
                .placeholder(R.drawable.news)
                .into(binding.ivNews)
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

    // BOOKMARK can access only in api
    override fun onDismiss(dialog: DialogInterface) {
        Log.d(TAG, "onDismiss: ${binding.btnBookmark.isChecked}")
        if(binding.btnBookmark.isChecked) {
            if(relatedNews != null){
                if (userId != -1) {
                    Log.d(TAG, "onDismiss: $userId")
                    val bookmark = Bookmark(
                        userId = userId,
                        author = relatedNews.author,
                        publishedAt = relatedNews.publishedAt,
                        urlToImage = relatedNews.urlToImage,
                        description = relatedNews.description,
                        content = relatedNews.content,
                        source = relatedNews.source,
                        title = relatedNews.title,
                        url = relatedNews.url,
                        id = 0,
                    )
//                    bookmarkVM.insertBookmark(bookmark)
                    bookmarkVM.postBookmarkToApi(bookmark)
                    Log.d(TAG, "onDismiss: Done")
                }
            }else if(headlineNews != null){
                if (userId != -1) {
                    Log.d(TAG, "onDismiss: $userId")
                    val bookmark = Bookmark(
                        userId = userId,
                        author = headlineNews.author,
                        publishedAt = headlineNews.publishedAt,
                        urlToImage = headlineNews.urlToImage,
                        description = headlineNews.description,
                        content = headlineNews.content,
                        source = headlineNews.source,
                        title = headlineNews.title,
                        url = headlineNews.url,
                        id = 0,
                    )
//                    bookmarkVM.insertBookmark(bookmark)
                    bookmarkVM.postBookmarkToApi(bookmark)
                    Log.d(TAG, "onDismiss: Done")
                }
            }
        }else{
            if(relatedNews == null && bookmark != null){
                if (userId != -1) {
                    Log.d(TAG, "onDismiss: $userId")
//                    bookmarkVM.deleteBookmark(bookmark)
                    bookmarkVM.deleteBookmarkUserFromApi(userId.toString(), bookmark.id)
                    Log.d(TAG, "onDismiss: Done")
                }
            }
        }
        super.onDismiss(dialog)
    }
}