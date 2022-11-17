package com.example.colega.worker_factory

import androidx.work.DelegatingWorkerFactory
import com.example.colega.api.NewsService
import com.example.colega.api.UserService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyWorkerFactory @Inject constructor(private val newsService: NewsService, private val userService: UserService): DelegatingWorkerFactory(){
//    init {
//        addFactory()
//    }
}