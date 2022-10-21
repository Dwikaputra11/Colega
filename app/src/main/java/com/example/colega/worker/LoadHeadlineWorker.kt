package com.example.colega.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.colega.api.NewsService
import com.example.colega.data.article.HeadlineNews
import com.example.colega.db.MyDatabase
import com.example.colega.models.news.ArticleResponse
import com.example.colega.models.news.NewsModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

private const val TAG = "LoadHeadlineWorker"
@Suppress("BlockingMethodInNonBlockingContext")
class LoadHeadlineWorker(
    context: Context,
    workerParams: WorkerParameters
): CoroutineWorker(context, workerParams) {

    private val application = applicationContext
    private val headlineDao = MyDatabase.getDatabase(application).headlineDao()
    @Inject
    lateinit var newsService: NewsService

    override suspend fun doWork(): Result {
        try {
            newsService.getTopHeadLines()
                .enqueue(object : Callback<NewsModel>{
                    override fun onResponse(call: Call<NewsModel>, response: Response<NewsModel>) {
                        if(response.isSuccessful){
                            response.body()?.let {
                                insertHeadlineToDB(it.articleResponses)
                            }
                        }else{
                            Log.d(TAG, "onResponse: Unsuccessfully")
                            throw Exception()
                        }
                    }

                    override fun onFailure(call: Call<NewsModel>, t: Throwable) {
                        Log.d(TAG, "onFailure: Failed to get data source")
                        throw Exception()
                    }

                })
            Log.d(TAG, "doWork: Success")
            return Result.success()
        }catch (e: Exception){
            return Result.failure()
        }
    }

    fun insertHeadlineToDB(list: List<ArticleResponse>){
        Log.d(TAG, "insertHeadlineToDB: Started")
        val headlines = list.map {
            HeadlineNews(
                id = 0,
                title = it.title,
                content = it.content,
                source = it.articleSource.name,
                url = it.url,
                publishedAt = it.publishedAt,
                author = it.author,
                urlToImage = it.urlToImage,
                isCheck = it.isCheck,
                description = it.description
            )
        }.toList()
        MyDatabase.databaseWriteExecutor.execute {
            headlineDao.deleteAllArticle()
            headlineDao.postArticle(headlines)
        }
        Log.d(TAG, "insertHeadlineToDB: Finished")
    }

}