package com.example.colega.data.article

import androidx.lifecycle.LiveData
import com.example.colega.db.MyDatabase
import javax.inject.Inject

class HeadlineRepository @Inject constructor(private val headlineDao: HeadlineDao) {

    fun getAllHeadline(): LiveData<List<HeadlineNews>> = headlineDao.getAllArticle()

    fun insertHeadline(headlineNews: List<HeadlineNews>){
        MyDatabase.databaseWriteExecutor.execute {
            headlineDao.postArticle(headlineNews)
        }
    }

    fun deleteAllHeadline(){
        MyDatabase.databaseWriteExecutor.execute {
            headlineDao.deleteAllArticle()
        }
    }
}