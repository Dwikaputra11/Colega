package com.example.colega.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.colega.api.RetrofitClient
import com.example.colega.data.article.RelatedNews
import com.example.colega.data.article.RelatedNewsRepository
import com.example.colega.db.MyDatabase
import com.example.colega.models.news.ArticleResponse
import com.example.colega.models.news.NewsModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "ArticleViewModel"
class ArticleViewModel(application: Application): AndroidViewModel(application) {
    private var articleResponseLiveData: MutableLiveData<List<ArticleResponse>> = MutableLiveData()
    private var headlineLiveData : MutableLiveData<List<ArticleResponse>> = MutableLiveData()
    private lateinit var relatedNewsRepository: RelatedNewsRepository

    fun getArticleLiveData(): MutableLiveData<List<ArticleResponse>> = articleResponseLiveData
    fun getHeadlineLiveData(): MutableLiveData<List<ArticleResponse>> = headlineLiveData

    init {
        val relatedNewsDao = MyDatabase.getDatabase(application).relatedNews()
        relatedNewsRepository = RelatedNewsRepository(relatedNewsDao)
    }

    fun getRelatedNews(){
        RetrofitClient.instanceFilm.getPreferences()
            .enqueue(object : Callback<NewsModel>{
                override fun onResponse(call: Call<NewsModel>, response: Response<NewsModel>) {
                    if(response.isSuccessful){
//                        Log.d(TAG, "onResponse: ${response.body()}")
                        if(response.body() != null){
                            articleResponseLiveData.postValue(response.body()!!.articleResponses)
//                            Log.d(TAG, "onResponse: ${response.body()!!.articles.size}")
                        }
                    }else{
                        Log.d(TAG, "onResponse: ${response.errorBody()}")
                    }
                }

                override fun onFailure(call: Call<NewsModel>, t: Throwable) {
                    Log.d(TAG, "onFailure: ${t.message}")
                }
            })
    }

    fun getHeadlineNews(){
        RetrofitClient.instanceFilm.getTopHeadLines()
            .enqueue(object : Callback<NewsModel>{
                override fun onResponse(call: Call<NewsModel>, response: Response<NewsModel>) {
                    if(response.isSuccessful){
                        if(response.body() != null){
                            headlineLiveData.postValue(response.body()!!.articleResponses)
                        }
                    }else{
                        Log.d(TAG, "onResponse: Failed")
                    }
                }

                override fun onFailure(call: Call<NewsModel>, t: Throwable) {
                    Log.d(TAG, "onFailure: Failed")
                }

            })
    }

    fun insertRelatedNewsToDB(relatedNews: RelatedNews){
        viewModelScope.launch {

        }
    }

}