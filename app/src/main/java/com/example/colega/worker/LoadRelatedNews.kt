package com.example.colega.worker

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.colega.data.article.RelatedNews
import com.example.colega.viewmodel.ArticleViewModel

class LoadRelatedNews(
    private val owner: ViewModelStoreOwner,
    private val lifecycleOwner: LifecycleOwner,
    context: Context,
    workerParams: WorkerParameters
): CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        try {
            // TODO: CHECK IF CONNECTION AVAILABLE IF NOT RETRY
            val articleVM = ViewModelProvider(owner)[ArticleViewModel::class.java]
            articleVM.getArticleLiveData().observe(lifecycleOwner){
                for(i in it.indices){
                    val article = RelatedNews(
                        id = 0,
                        title = it[i].title,
                        content = it[i].content,
                        source = it[i].articleSource.name,
                        url = it[i].url,
                        publishedAt = it[i].publishedAt,
                        author = it[i].author,
                        urlToImage = it[i].urlToImage,
                        isCheck = it[i].isCheck,
                        description = it[i].description
                    )
                    articleVM.insertRelatedNewsToDB(article)
                }
            }
            return Result.success()
        }catch (e: Exception){
            return Result.failure()
        }
    }
}