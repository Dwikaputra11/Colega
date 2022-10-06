package com.example.colega.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.colega.R
import com.example.colega.adapter.HeadlineAdapter
import com.example.colega.adapter.RelatedNewsAdapter
import com.example.colega.data.News
import com.example.colega.databinding.FragmentFeedsBinding
import com.example.colega.dummy.DummyData

class FeedsFragment : Fragment() {
    private  val TAG = "FeedsFragment"
    private lateinit var binding: FragmentFeedsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.vpHeadline.offscreenPageLimit = DummyData.headline.size
        binding.vpHeadline.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.vpHeadline.adapter = HeadlineAdapter(DummyData.headline)
        binding.vpHeadline.setPageTransformer(MarginPageTransformer(50))
        binding.vpHeadline.clipToPadding = false;
        binding.vpHeadline.setPadding(10,10,10,0);

        val relatedAdapter = RelatedNewsAdapter(DummyData.relatedNews)
        binding.rvForYou.adapter = relatedAdapter
        binding.rvForYou.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        relatedAdapter.setOnItemClickListener(object : RelatedNewsAdapter.OnItemClickListener{
            override fun onItemClick(news: News) {
                val newsDetailFragment = NewsDetailFragment(news)
                newsDetailFragment.show(requireActivity().supportFragmentManager, newsDetailFragment.tag)
            }
        })
    }

}