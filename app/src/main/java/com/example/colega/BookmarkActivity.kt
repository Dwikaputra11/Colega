package com.example.colega

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.colega.adapter.BookmarkAdapter
import com.example.colega.data.Bookmark
import com.example.colega.databinding.ActivityBookmarkBinding
import com.example.colega.utils.Utils
import com.example.colega.viewmodel.BookmarkViewModel

class BookmarkActivity : AppCompatActivity() {
    private lateinit var bookmarkVM: BookmarkViewModel
    private lateinit var binding: ActivityBookmarkBinding
    private lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = this.getSharedPreferences(Utils.name, Context.MODE_PRIVATE)
        bookmarkVM = ViewModelProvider(this)[BookmarkViewModel::class.java]
        binding.rvBookmark.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val adapter = BookmarkAdapter()
        binding.rvBookmark.adapter = adapter
        val userId = sharedPref.getInt(Utils.userId, -1)
        if(userId != -1){
            bookmarkVM.getAllBookmark(userId).observe(this){
                if(it != null){
                    adapter.setBookmarkList(it)
                    binding.shimmerLayout.startShimmer()
                    binding.shimmerLayout.visibility = View.GONE
                    binding.rvBookmark.visibility = View.VISIBLE
                }
            }
        }
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