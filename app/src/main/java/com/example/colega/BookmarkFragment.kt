package com.example.colega

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
import com.example.colega.databinding.ActivityBookmarkBinding
import com.example.colega.databinding.FragmentBookmarkBinding
import com.example.colega.utils.Utils
import com.example.colega.viewmodel.BookmarkViewModel

class BookmarkFragment : Fragment() {
    private lateinit var binding: FragmentBookmarkBinding
    private lateinit var bookmarkVM: BookmarkViewModel
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
        binding.rvBookmark.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val adapter = BookmarkAdapter()
        binding.rvBookmark.adapter = adapter
        val userId = sharedPref.getInt(Utils.userId, -1)
        if(userId != -1){
            bookmarkVM.getAllBookmark(userId).observe(viewLifecycleOwner){
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