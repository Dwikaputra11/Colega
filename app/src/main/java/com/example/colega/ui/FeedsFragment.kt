package com.example.colega.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.colega.adapter.RelatedNewsAdapter
import com.example.colega.databinding.FragmentFeedsBinding
import com.example.colega.models.news.Article
import com.example.colega.utils.Utils
import com.example.colega.viewmodel.ArticleViewModel
import com.example.colega.viewmodel.BookmarkViewModel

class FeedsFragment : Fragment() {
    private  val TAG = "FeedsFragment"
    private lateinit var binding: FragmentFeedsBinding
    private lateinit var bookmarkVM: BookmarkViewModel
    private lateinit var sharedPref: SharedPreferences

    private lateinit var articleVM: ArticleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        articleVM = ViewModelProvider(this)[ArticleViewModel::class.java]
        sharedPref = requireActivity().getSharedPreferences(Utils.name, Context.MODE_PRIVATE)
        bookmarkVM = ViewModelProvider(this)[BookmarkViewModel::class.java]
        val relatedAdapter = RelatedNewsAdapter()
        binding.rvForYou.adapter = relatedAdapter
        binding.rvForYou.layoutManager = object : LinearLayoutManager(binding.root.context){
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        relatedAdapter.setOnItemClickListener(object : RelatedNewsAdapter.OnItemClickListener{
            override fun onItemClick(news: Article) {
                val newsDetailFragment = NewsDetailFragment(news, null)
                newsDetailFragment.show(requireActivity().supportFragmentManager, newsDetailFragment.tag)
            }
        })
        articleVM.getRelatedNews()
        articleVM.getArticleLiveData().observe(viewLifecycleOwner){
            if(it != null){
                relatedAdapter.setRelatedNews(it)
                binding.shimmerLayout.startShimmer()
                binding.shimmerLayout.visibility = View.GONE
                binding.rvForYou.visibility = View.VISIBLE
            }
        }

        binding.tvSeeMore.setOnClickListener {

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