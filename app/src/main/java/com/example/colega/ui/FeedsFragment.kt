package com.example.colega.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.colega.adapter.HeadlineAdapter
import com.example.colega.adapter.RelatedNewsAdapter
import com.example.colega.data.News
import com.example.colega.databinding.FragmentFeedsBinding
import com.example.colega.dummy.DummyData
import com.example.colega.utils.Utils
import com.example.colega.viewmodel.ArticleViewModel
import com.github.ayodkay.builder.EverythingBuilder
import com.github.ayodkay.client.NewsApiClient
import com.github.ayodkay.init.NewsApi
import com.github.ayodkay.interfaces.ArticlesResponseCallback
import com.github.ayodkay.models.Article
import com.github.ayodkay.models.ArticleResponse
import com.github.ayodkay.models.NetworkInterceptorModel
import com.github.ayodkay.models.OfflineCacheInterceptorModel
import com.github.ayodkay.mvvm.client.NewsApiClientWithObserver
import com.github.ayodkay.mvvm.interfaces.ArticlesLiveDataResponseCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.concurrent.TimeUnit

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

        NewsApi.init(requireActivity())
        val newsApi = NewsApiClientWithObserver(Utils.apiKey,NetworkInterceptorModel(1, TimeUnit.MINUTES), OfflineCacheInterceptorModel(1, TimeUnit.DAYS))
        val everything = EverythingBuilder.Builder()
            .q("bitcoin")
            .sortBy("popularity")
            .build()

        newsApi.getEverything(
            everything,
            object : ArticlesLiveDataResponseCallback {
                override fun onFailure(throwable: Throwable) {
                    TODO("Not yet implemented")
                }

                override fun onSuccess(response: MutableLiveData<ArticleResponse>) {
                    response.observe(viewLifecycleOwner){ it ->
                        Log.d(TAG, "onSuccess articles: ${it.articles}")
                        Log.d(TAG, "onSuccess articles size: ${it.articles.size}")
                        Log.d(TAG, "onSuccess status: ${it.status}")
                        Log.d(TAG, "onSuccess totalResults: ${it.totalResults}")
                        Log.d(TAG, "-------------------------------------------------")
                        if(response.value != null){
                            response.value?.articles?.let { it1 -> articleVM.getRelatedNews(it1.toList()) }
                            articleVM.getArticleLiveData().observe(viewLifecycleOwner){ article ->
                                if(it != null){
                                    Log.d(TAG, "onViewCreated: NOT NULL")
                                    relatedAdapter.setRelatedNews(article)
                                }
                            }
                        }
                    }
                }
            }
        )
    }

}