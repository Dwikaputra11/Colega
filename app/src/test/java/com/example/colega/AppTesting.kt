package com.example.colega

import com.example.colega.api.NewsService
import com.example.colega.api.UserService
import com.example.colega.models.news.NewsModel
import com.example.colega.models.news.SourceResponse
import com.example.colega.models.user.DataUser
import com.example.colega.models.user.UserBookmark
import com.example.colega.models.user.UserFollowingSource
import com.example.colega.models.user.UserResponseItem
import com.example.colega.utils.Utils
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Call

class AppTesting {

    lateinit var userService: UserService
    lateinit var newsService: NewsService


    @Before
    fun setUp(){
        userService = mockk()
        newsService = mockk()
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
}