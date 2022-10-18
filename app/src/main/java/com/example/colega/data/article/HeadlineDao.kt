package com.example.colega.data.article

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.Query

interface HeadlineDao {

    @Query("SELECT * FROM headline_news")
    fun getAllArticle(): LiveData<List<HeadlineNews>>

    @Query("DELETE FROM headline_news")
    fun deleteAllArticle()

    @Insert
    fun postArticle(headlineNews: HeadlineNews)
}