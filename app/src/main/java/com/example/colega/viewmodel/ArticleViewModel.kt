package com.example.colega.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.colega.utils.Utils
import com.github.ayodkay.builder.EverythingBuilder
import com.github.ayodkay.client.NewsApiClient
import com.github.ayodkay.interfaces.ArticlesResponseCallback
import com.github.ayodkay.models.Article
import com.github.ayodkay.models.ArticleResponse

class ArticleViewModel: ViewModel() {
    private val TAG = "ArticleViewModel"
    private var articleLiveData: MutableLiveData<List<Article>> = MutableLiveData()

    fun getArticleLiveData(): MutableLiveData<List<Article>> = articleLiveData

    fun getRelatedNews(list: List<Article>){
        Log.d(TAG, "getRelatedNews: ${list.size}")
        articleLiveData.postValue(list)
    }

}