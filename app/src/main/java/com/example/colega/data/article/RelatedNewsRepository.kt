package com.example.colega.data.article

import androidx.lifecycle.LiveData
import com.example.colega.db.MyDatabase
import javax.inject.Inject

class RelatedNewsRepository @Inject constructor(private val relatedNewsDao: RelatedNewsDao) {

    fun getAllRelatedNews(): LiveData<List<RelatedNews>> = relatedNewsDao.getAllArticle()

    fun updateRelatedNews(relatedNews: RelatedNews){
        MyDatabase.databaseWriteExecutor.execute {
            relatedNewsDao.updateArticle(relatedNews)
        }
    }

    fun insertRelatedNews(relatedNews: RelatedNews){
        MyDatabase.databaseWriteExecutor.execute {
//            relatedNewsDao.postArticle(relatedNews)
        }
    }

    fun deleteAllRelatedNews(){
        MyDatabase.databaseWriteExecutor.execute {
            relatedNewsDao.deleteAllArticle()
        }
    }
}