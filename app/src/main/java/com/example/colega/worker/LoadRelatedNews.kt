package com.example.colega.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.colega.api.NewsService
import com.example.colega.api.SingletonInstance
import com.example.colega.data.article.RelatedNews
import com.example.colega.data.article.RelatedNewsDao
import com.example.colega.db.MyDatabase
import com.example.colega.models.news.ArticleResponse
import com.example.colega.models.news.NewsModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


private const val TAG = "LoadRelatedNews"
@Suppress("BlockingMethodInNonBlockingContext")
class LoadRelatedNews constructor(
    context: Context,
    workerParams: WorkerParameters,
): CoroutineWorker(context, workerParams) {

    private val relatedNewsDao = MyDatabase.getDatabase(applicationContext).relatedNewsDao()

    override suspend fun doWork(): Result {
        Log.d(TAG, "doWork: Started")
        try {
            // TODO: CHECK IF CONNECTION AVAILABLE IF NOT RETRY
            Log.d(TAG, "doWork: Fetching Data")

            SingletonInstance.instanceNews.getPreferences()
                .enqueue(object : Callback<NewsModel> {
                    override fun onResponse(call: Call<NewsModel>, response: Response<NewsModel>) {
                        if(response.isSuccessful) {
                            Log.d(TAG, "onResponse: Data Success")
                            if (response.body() != null) {
                                postArticleToDB(response.body()!!.articleResponses)
                            }
                        }else{
                            Log.d(TAG, "onResponse: Data Failed")
                            throw Exception()
                        }
                    }

                    override fun onFailure(call: Call<NewsModel>, t: Throwable) {
                        Log.d(TAG, "onFailure: Failed to Fetch")
                        throw Exception()
                    }

                })
            Log.d(TAG, "doWork: Success")
            return Result.success()
        }catch (e: Exception){
            Log.d(TAG, "doWork: Failed")
            return Result.failure()
        }
    }

    fun postArticleToDB(it: List<ArticleResponse>){
        val articles = it.map { list ->
            RelatedNews(
                id = 0,
                title = list.title,
                content =  list.content,
                source = list.articleSource.name,
                url = list.url,
                publishedAt = list.publishedAt,
                author = list.author,
                urlToImage = list.urlToImage,
                isCheck = list.isCheck,
                description = list.description
            )
        }.toList()
        MyDatabase.databaseWriteExecutor.execute {
            relatedNewsDao.deleteAllArticle()
            relatedNewsDao.postArticle(articles)
        }
        Log.d(TAG, "postArticleToDB: $articles")
    }
}