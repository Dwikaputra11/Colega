package com.example.colega.data.article

import androidx.lifecycle.LiveData
import com.example.colega.db.MyDatabase

class RelatedNewsRepository(private val relatedNewsDao: RelatedNewsDao) {

    fun getAllRelatedNews(): LiveData<List<RelatedNews>> = relatedNewsDao.getAllArticle()

    fun insertRelatedNews(relatedNews: RelatedNews){
        MyDatabase.databaseWriteExecutor.execute {
            relatedNewsDao.postArticle(relatedNews)
        }
    }

    fun deleteAllRelatedNews(){
        MyDatabase.databaseWriteExecutor.execute {
            relatedNewsDao.deleteAllArticle()
        }
    }
}