package com.example.colega.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.colega.adapter.HeadlineAdapter
import com.example.colega.adapter.RelatedNewsAdapter
import com.example.colega.databinding.FragmentFeedsBinding
import com.example.colega.dummy.DummyData
import com.example.colega.models.Article
import com.example.colega.viewmodel.ArticleViewModel

class FeedsFragment : Fragment() {
    private  val TAG = "FeedsFragment"
    private lateinit var binding: FragmentFeedsBinding
    private var relatedNewsList: List<Article> = emptyList()

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

        binding.vpHeadline.offscreenPageLimit = 3
        binding.vpHeadline.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.vpHeadline.adapter = HeadlineAdapter(DummyData.headline)
        binding.vpHeadline.canScrollHorizontally(1)
        binding.vpHeadline.setPageTransformer(MarginPageTransformer(50))
        binding.vpHeadline.clipToPadding = false;
        binding.vpHeadline.setPadding(10,10,10,0);
        binding.dotsIndicator.attachTo(binding.vpHeadline)

        val relatedAdapter = RelatedNewsAdapter()
        binding.rvForYou.adapter = relatedAdapter
        binding.rvForYou.layoutManager = object : LinearLayoutManager(binding.root.context){
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        relatedAdapter.setOnItemClickListener(object : RelatedNewsAdapter.OnItemClickListener{
            override fun onItemClick(news: Article) {
                val newsDetailFragment = NewsDetailFragment(news)
                newsDetailFragment.show(requireActivity().supportFragmentManager, newsDetailFragment.tag)
            }
        })
        articleVM.getRelatedNews()
        articleVM.getArticleLiveData().observe(viewLifecycleOwner){
            if(it != null){
                relatedAdapter.setRelatedNews(it)
            }
        }

        binding.tvSeeMore.setOnClickListener {

        }
    }

}