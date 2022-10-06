package com.example.colega.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.colega.api.RetrofitClient
import com.example.colega.models.Article
import com.example.colega.models.NewsModel
import com.example.colega.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticleViewModel: ViewModel() {
    private val TAG = "ArticleViewModel"
    private var articleLiveData: MutableLiveData<List<Article>> = MutableLiveData()

    fun getArticleLiveData(): MutableLiveData<List<Article>> = articleLiveData

    fun getRelatedNews(){
        RetrofitClient.instance.getPreferences()
            .enqueue(object : Callback<NewsModel>{
                override fun onResponse(call: Call<NewsModel>, response: Response<NewsModel>) {
                    if(response.isSuccessful){
                        Log.d(TAG, "onResponse: ${response.body()}")
                        if(response.body() != null){
                            articleLiveData.postValue(response.body()!!.articles)
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

}