package com.example.colega.api

import android.app.Application
import com.example.colega.data.article.HeadlineDao
import com.example.colega.data.article.RelatedNewsDao
import com.example.colega.data.source.SourceDao
import com.example.colega.db.MyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SingletonInstance {

    private const val BASE_URL_NEWS = "https://newsapi.org/v2/"
    private const val BASE_URL_USER = "https://6347ce8d0484786c6e892194.mockapi.io/colega/v1/"

    private val interceptor: HttpLoggingInterceptor
        get(){
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            return httpLoggingInterceptor.apply {
                httpLoggingInterceptor.level  = HttpLoggingInterceptor.Level.BODY
            }
        }

    private val client = OkHttpClient.Builder().addInterceptor(interceptor).build()


    val instanceNews: NewsService by lazy {
        val interceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
        }.build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_NEWS)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(NewsService::class.java)
    }

    val instanceUser: UserService by lazy {
        val interceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
        }.build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_USER)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(UserService::class.java)
    }

    @Singleton
    @Provides
    fun providesUserService(): UserService =
        Retrofit.Builder()
            .baseUrl(BASE_URL_USER)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserService::class.java)

    @Singleton
    @Provides
    fun providesFilmService(): NewsService =
        Retrofit.Builder()
            .baseUrl(BASE_URL_NEWS)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsService::class.java)

    @Singleton
    @Provides
    fun createDB(context: Application): MyDatabase =
        MyDatabase.getDatabase(context)

    @Singleton
    @Provides
    fun getRelatedDao(myDatabase: MyDatabase): RelatedNewsDao =
        myDatabase.relatedNewsDao()

    @Singleton
    @Provides
    fun getHeadlineDao(myDatabase: MyDatabase): HeadlineDao =
        myDatabase.headlineDao()

    @Singleton
    @Provides
    fun getSourceDao(myDatabase: MyDatabase): SourceDao =
        myDatabase.sourceDao()
}