package com.example.colega.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.colega.adapter.BookmarkAdapter
import com.example.colega.data.users.Bookmark
import com.example.colega.databinding.FragmentBookmarkBinding
import com.example.colega.models.user.UserBookmark
import com.example.colega.utils.Utils
import com.example.colega.viewmodel.BookmarkViewModel
import com.example.colega.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "BookmarkFragment"

@AndroidEntryPoint
class BookmarkFragment : Fragment() {
    private lateinit var binding: FragmentBookmarkBinding
    private lateinit var bookmarkVM: BookmarkViewModel
    private lateinit var userVM: UserViewModel
    private lateinit var sharedPref: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookmarkBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedPref = requireActivity().getSharedPreferences(Utils.name, Context.MODE_PRIVATE)
        bookmarkVM = ViewModelProvider(this)[BookmarkViewModel::class.java]
        userVM = ViewModelProvider(this)[UserViewModel::class.java]
        requireActivity().runOnUiThread {
            setViews()
        }
    }

    private fun setViews() {
        binding.rvBookmark.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val adapter = BookmarkAdapter()
        binding.rvBookmark.adapter = adapter
        var userId = -1
        userVM.dataUser.observe(requireActivity()){
            userId = it.userId
        }
        if(userId != -1){
            bookmarkVM.getBookmarkFromApi(userId.toString())
            bookmarkVM.getUserBookmark().observe(viewLifecycleOwner){
                if(it != null){
                    adapter.setBookmarkList(it)
                    binding.shimmerLayout.startShimmer()
                    binding.shimmerLayout.visibility = View.GONE
                    binding.rvBookmark.visibility = View.VISIBLE
                }
            }
        }
        adapter.setOnItemClickListener(object : BookmarkAdapter.OnItemClickListener{
            override fun onItemClick(news: UserBookmark) {
                val newsDetailFragment = NewsDetailFragment(null, null, news)
                newsDetailFragment.show(requireActivity().supportFragmentManager, newsDetailFragment.tag)
            }
        })
    }

    override fun onStart() {
        binding.shimmerLayout.startShimmer()
        super.onStart()
    }

    override fun onPause() {
        binding.shimmerLayout.stopShimmer()
        super.onPause()
    }
}