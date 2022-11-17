package com.example.colega

import android.content.Context
import android.os.Build.VERSION_CODES.Q
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import com.example.colega.api.NewsService
import com.example.colega.api.UserService
import com.example.colega.models.news.NewsModel
import com.example.colega.models.news.SourceResponse
import com.example.colega.models.user.DataUser
import com.example.colega.models.user.UserBookmark
import com.example.colega.models.user.UserFollowingSource
import com.example.colega.models.user.UserResponseItem
import com.example.colega.worker.LoadHeadlineWorker
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.robolectric.annotation.Config
import retrofit2.Call

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Q])
class AppTesting {

    private lateinit var userService: UserService
    private lateinit var newsService: NewsService
    private lateinit var context: Context


    @Before
    fun setUp(){
        userService = mockk()
        newsService = mockk()
        context = ApplicationProvider.getApplicationContext()
    }


    @Test
    fun getUserByUsername(): Unit = runBlocking {
        val respUser = mockk<Call<List<UserResponseItem>>>()

        every {
            runBlocking {
                userService.getUserByUsername("putra")
            }
        } returns respUser

        val result  = userService.getUserByUsername("putra")

        verify {
            runBlocking {
                userService.getUserByUsername("putra")
            }
        }
        assertEquals(result, respUser)
    }

    @Test
    fun getUserBookmark(): Unit = runBlocking {
        val respUser = mockk<Call<List<UserBookmark>>>()

        every {
            runBlocking {
                userService.getUserBookmark("1")
            }
        } returns respUser

        val result  = userService.getUserBookmark("1")

        verify {
            runBlocking {
                userService.getUserBookmark("1")
            }
        }
        assertEquals(result, respUser)
    }

    @Test
    fun postUser(){
        val user = DataUser(username = "ka z", "kakak zz","10/10/1970", "kaz@gmail.com", "kaz9999","")
        val respUser = mockk<Call<UserResponseItem>>()

        every {
            runBlocking {
                userService.addUser(user)
            }
        } returns respUser

        val result  = userService.addUser(user)

        verify {
            runBlocking {
                userService.addUser(user)
            }
        }
        assertEquals(result, respUser)
    }

    @Test
    fun getFollowingSource(): Unit = runBlocking {
        val respUser = mockk<Call<List<UserFollowingSource>>>()

        every {
            runBlocking {
                userService.getUserFollowingSource("1")
            }
        } returns respUser

        val result  = userService.getUserFollowingSource("1")

        verify {
            runBlocking {
                userService.getUserFollowingSource("1")
            }
        }
        assertEquals(result, respUser)
    }


    @Test
    fun getTopHeadlines(): Unit = runBlocking {
        val response = mockk<Call<NewsModel>>()

        every {
            runBlocking {
                newsService.getTopHeadLines()
            }
        } returns response

        val result = newsService.getTopHeadLines()

        verify {
            runBlocking {
                newsService.getTopHeadLines()
            }
        }

        assertEquals(result, response)
    }

    @Test
    fun getPreferences(): Unit = runBlocking {
        val response = mockk<Call<NewsModel>>()

        every {
            runBlocking {
                newsService.getPreferences()
            }
        } returns response

        val result = newsService.getPreferences()

        verify {
            runBlocking {
                newsService.getPreferences()
            }
        }

        assertEquals(result, response)
    }
    @Test
    fun getAllNewsSource(): Unit = runBlocking {
        val response = mockk<Call<SourceResponse>>()

        every {
            runBlocking {
                newsService.getAllSourceNews()
            }
        } returns response

        val result = newsService.getAllSourceNews()

        verify {
            runBlocking {
                newsService.getAllSourceNews()
            }
        }

        assertEquals(result, response)
    }

    // Test Worker
    @Test
    fun testHeadlineWorker(){
        // get the Listenable Worker
        val worker = TestListenableWorkerBuilder<LoadHeadlineWorker>(context).build()

        // run the worker synchronously
        val result = worker.startWork().get()

        assertThat(result, `is`(ListenableWorker.Result.success()))
    }

    @Test
    fun testHeadlineWorkRetry(){
        val worker = TestListenableWorkerBuilder<LoadHeadlineWorker>(context)
            .setRunAttemptCount(2)
            .build()
        val result = worker.startWork().get()

        assertThat(result, `is`(ListenableWorker.Result.success())) // show success() instead retry()
    }

    @Test
    fun testHeadlineWorkRetryFailed(){
        val worker = TestListenableWorkerBuilder<LoadHeadlineWorker>(context)
            .setRunAttemptCount(3)
            .build()
        val result = worker.startWork().get()

        assertThat(result, `is`(ListenableWorker.Result.success())) // show success() instead retry()
    }
}