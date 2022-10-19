package com.example.colega.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.example.colega.api.RetrofitClient
import com.example.colega.data.article.HeadlineNews
import com.example.colega.data.article.HeadlineRepository
import com.example.colega.data.article.RelatedNews
import com.example.colega.data.article.RelatedNewsRepository
import com.example.colega.db.MyDatabase
import com.example.colega.models.news.ArticleResponse
import com.example.colega.models.news.NewsModel
import com.example.colega.worker.LoadHeadlineWorker
import com.example.colega.worker.LoadRelatedNews
import com.example.colega.worker.WorkerKeys
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

private const val TAG = "ArticleViewModel"
class ArticleViewModel(application: Application): AndroidViewModel(application) {
    private var articleResponseLiveData: MutableLiveData<List<ArticleResponse>> = MutableLiveData()
    private var headlineLiveData : MutableLiveData<List<ArticleResponse>> = MutableLiveData()
    private val relatedNewsRepository: RelatedNewsRepository
    private val headlineRepository: HeadlineRepository
    private val workManager: WorkManager
    private val relatedWorkInfo: LiveData<List<WorkInfo>>
    private val headlineWorkInfo: LiveData<List<WorkInfo>>

    fun getArticleLiveData(): MutableLiveData<List<ArticleResponse>> = articleResponseLiveData
    fun getHeadlineLiveData(): MutableLiveData<List<ArticleResponse>> = headlineLiveData

    init {
        val relatedNewsDao = MyDatabase.getDatabase(application).relatedNews()
        relatedNewsRepository = RelatedNewsRepository(relatedNewsDao)
        val headlineDao = MyDatabase.getDatabase(application).headlineDao()
        headlineRepository = HeadlineRepository(headlineDao)
        workManager = WorkManager.getInstance(application)
        relatedWorkInfo = workManager.getWorkInfosByTagLiveData(WorkerKeys.TAG_RELATED_NEWS_DATA)
        headlineWorkInfo = workManager.getWorkInfosByTagLiveData(WorkerKeys.TAG_HEADLINE_NEWS_DATA)
    }

    @SuppressLint("RestrictedApi")
    fun fetchRelatedNews(){
        Log.d(TAG, "fetchRelatedNews: Started")
        // Create Network constraint
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicSyncDataWork =
            PeriodicWorkRequest.Builder(LoadRelatedNews::class.java, 1, TimeUnit.HOURS)
                .addTag(WorkerKeys.TAG_RELATED_NEWS_DATA)
                .setConstraints(constraints) // setting a backoff on case the work needs to retry
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS
                )
                .build()
        workManager.enqueueUniquePeriodicWork(
            WorkerKeys.SYNC_RELATED_NEWS_DATA,
            ExistingPeriodicWorkPolicy.KEEP,  //Existing Periodic Work policy
            periodicSyncDataWork //work request
        )
        Log.d(TAG, "fetchRelatedNews: ${periodicSyncDataWork.workSpec.isPeriodic}")
    }

    fun fetchHeadlineNews(){
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicSyncDataWork =
            PeriodicWorkRequest.Builder(LoadHeadlineWorker::class.java, 1, TimeUnit.HOURS)
                .addTag(WorkerKeys.TAG_HEADLINE_NEWS_DATA)
                .setConstraints(constraints)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS
                )
                .build()

        workManager.enqueueUniquePeriodicWork(
            WorkerKeys.SYNC_HEADLINE_NEWS_DATA,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicSyncDataWork
        )
    }

    fun getRelatedWorkInfo(): LiveData<List<WorkInfo>> = relatedWorkInfo
    fun getHeadlineWorkInfo(): LiveData<List<WorkInfo>> = headlineWorkInfo

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

    fun getAlRelatedNewsFromDB(): LiveData<List<RelatedNews>> = relatedNewsRepository.getAllRelatedNews()

    fun getAllHeadlineNewsFromDB(): LiveData<List<HeadlineNews>> = headlineRepository.getAllHeadline()

    fun insertRelatedNewsToDB(relatedNews: RelatedNews){
        viewModelScope.launch {
            relatedNewsRepository.insertRelatedNews(relatedNews)
        }
    }

    fun deleteAllRelatedFromDB(){
        viewModelScope.launch {
            relatedNewsRepository.deleteAllRelatedNews()
        }
    }

    fun insertHeadlineToDB(headlineNews: List<HeadlineNews>){
        viewModelScope.launch {
            headlineRepository.insertHeadline(headlineNews)
        }
    }

    fun deleteAllHeadlineFromDB(){
        viewModelScope.launch {
            headlineRepository.deleteAllHeadline()
        }
    }

}