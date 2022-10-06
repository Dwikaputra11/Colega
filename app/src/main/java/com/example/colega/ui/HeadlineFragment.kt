package com.example.colega.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.colega.R
import com.example.colega.adapter.HeadlineAdapter
import com.example.colega.databinding.FragmentHeadlineBinding
import com.example.colega.databinding.HeadlineItemBinding
import com.example.colega.models.Article
import com.example.colega.viewmodel.ArticleViewModel


class HeadlineFragment : Fragment() {
    private val TAG = "HeadlineFragment"
    private lateinit var binding: FragmentHeadlineBinding
    private lateinit var articleVM: ArticleViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHeadlineBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        articleVM = ViewModelProvider(this)[ArticleViewModel::class.java]
        val adapter = HeadlineAdapter()
        binding.rvHeadline.adapter = adapter
        binding.rvHeadline.layoutManager = object: LinearLayoutManager(binding.root.context){
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        articleVM.getHeadlineNews()
        articleVM.getHeadlineLiveData().observe(viewLifecycleOwner){
            if(it != null){
                adapter.setHeadlineList(it)
                Log.d(TAG, "onViewCreated: ${it.size}")
            }
        }
        adapter.setOnItemClickListener(object : HeadlineAdapter.OnItemClickListener{
            override fun onItemClick(news: Article) {
                val newsDetailFragment = NewsDetailFragment(news)
                newsDetailFragment.show(requireActivity().supportFragmentManager, newsDetailFragment.tag)
            }

        })

    }


}