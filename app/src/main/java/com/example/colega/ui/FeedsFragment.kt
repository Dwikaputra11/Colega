package com.example.colega.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.WorkInfo
import com.example.colega.adapter.RelatedNewsAdapter
import com.example.colega.data.article.RelatedNews
import com.example.colega.databinding.FragmentFeedsBinding
import com.example.colega.utils.Utils
import com.example.colega.viewmodel.ArticleViewModel
import com.example.colega.viewmodel.BookmarkViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedsFragment : Fragment() {
    private  val TAG = "FeedsFragment"
    private lateinit var binding: FragmentFeedsBinding
    private lateinit var bookmarkVM: BookmarkViewModel
    private lateinit var sharedPref: SharedPreferences
    private lateinit var relatedAdapter: RelatedNewsAdapter

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
        relatedAdapter = RelatedNewsAdapter()
        binding.rvForYou.adapter = relatedAdapter
        binding.rvForYou.layoutManager = object : LinearLayoutManager(binding.root.context){
            override fun canScrollVertically(): Boolean {
                return true
            }
        }
        relatedAdapter.setOnItemClickListener(object : RelatedNewsAdapter.OnItemClickListener{
            override fun onItemClick(news: RelatedNews) {
                val newsDetailFragment = NewsDetailFragment(null,news, null)
                newsDetailFragment.show(requireActivity().supportFragmentManager, newsDetailFragment.tag)
            }
        })

        requestNews()
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

    @SuppressLint("RestrictedApi")
    fun requestNews(){
        articleVM.fetchRelatedNews()
        articleVM.getRelatedWorkInfo().observe(viewLifecycleOwner){
            val workInfo = it[0]
            if(workInfo.state == WorkInfo.State.ENQUEUED || workInfo.state == WorkInfo.State.SUCCEEDED){
                binding.shimmerLayout.startShimmer()
                articleVM.getAlRelatedNewsFromDB().observe(viewLifecycleOwner){ list ->
                    relatedAdapter.setRelatedNews(list)
                    binding.shimmerLayout.startShimmer()
                    binding.shimmerLayout.visibility = View.GONE
                    binding.rvForYou.visibility = View.VISIBLE
                }
            }
        }
    }
}