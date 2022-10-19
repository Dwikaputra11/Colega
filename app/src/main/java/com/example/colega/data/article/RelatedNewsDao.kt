package com.example.colega.data.article

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RelatedNewsDao {
    @Query("SELECT * FROM related_news")
    fun getAllArticle(): LiveData<List<RelatedNews>>

    @Query("DELETE FROM related_news")
    fun deleteAllArticle()

    @Insert
    fun postArticle(relatedNewsList: List<RelatedNews>)
}