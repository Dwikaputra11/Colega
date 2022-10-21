package com.example.colega.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.WorkInfo
import com.example.colega.adapter.HeadlineAdapter
import com.example.colega.data.article.HeadlineNews
import com.example.colega.databinding.FragmentHeadlineBinding
import com.example.colega.models.news.ArticleResponse
import com.example.colega.viewmodel.ArticleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HeadlineFragment : Fragment() {
    private val TAG = "HeadlineFragment"
    private lateinit var binding: FragmentHeadlineBinding
    private lateinit var articleVM: ArticleViewModel
    private lateinit var adapter: HeadlineAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHeadlineBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        articleVM = ViewModelProvider(this)[ArticleViewModel::class.java]
        adapter = HeadlineAdapter()
        binding.rvHeadline.adapter = adapter
        binding.rvHeadline.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
//        articleVM.getHeadlineNews()
//        articleVM.getHeadlineLiveData().observe(viewLifecycleOwner){
//            if(it != null){
//                adapter.setHeadlineList(it)
//                binding.shimmerLayout.startShimmer()
//                binding.shimmerLayout.visibility = View.GONE
//                binding.rvHeadline.visibility = View.VISIBLE
//                Log.d(TAG, "onViewCreated: ${it.size}")
//            }
//        }
        requestHeadlines()
        adapter.setOnItemClickListener(object : HeadlineAdapter.OnItemClickListener{
            override fun onItemClick(news: HeadlineNews) {
                val newsDetailFragment = NewsDetailFragment(news, null, null)
                newsDetailFragment.show(requireActivity().supportFragmentManager, newsDetailFragment.tag)
            }
        })
    }

    fun requestHeadlines(){
        articleVM.fetchHeadlineNews()
        articleVM.getHeadlineWorkInfo().observe(viewLifecycleOwner){
            val workInfo = it[0]
            if(workInfo.state.isFinished || workInfo.state == WorkInfo.State.ENQUEUED){
                articleVM.getAllHeadlineNewsFromDB().observe(viewLifecycleOwner){ list ->
                    if(list != null){
                        binding.shimmerLayout.startShimmer()
                        adapter.setHeadlineList(list)
                        binding.shimmerLayout.visibility = View.GONE
                        binding.rvHeadline.visibility = View.VISIBLE
                    }
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