package com.example.colega.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.JsonReader
import android.util.Log
import com.example.colega.R
import com.example.colega.helper.ArticleDeserialize
import com.example.colega.models.news.ArticleResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.StringReader
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object UtilMethods {
    @SuppressLint("SimpleDateFormat")
    fun convertISOTime(context: Context, isoTime: String?): String {

        val sdf = SimpleDateFormat(context.getString(R.string.default_time_format))
        var convertedDate: Date? = null
        var formattedDate: String? = null
        var formattedTime: String = "10:00 AM"
        try {
            convertedDate = sdf.parse(isoTime)
            formattedDate = convertedDate?.let {
                SimpleDateFormat(context.getString(R.string.date_format)).format(
                    it
                )
            }
            formattedTime = convertedDate?.let {
                SimpleDateFormat(context.getString(R.string.time_format)).format(
                    it
                )
            }.toString()

//            Log.e("Time", formattedTime.toString())

            if((formattedTime.subSequence(6,8).toString() == "PM" || formattedTime.subSequence(6,8).toString() == "pm") && formattedTime.subSequence(0,2).toString().toInt()>12){
                formattedTime = (formattedTime.subSequence(0,2).toString().toInt()-12).toString()+formattedTime.subSequence(2,8).toString()
            }
            if (formattedTime.subSequence(0,2).toString().equals("00")){
                formattedTime = (formattedTime.subSequence(0,2).toString().toInt()+1).toString()+formattedTime.subSequence(2,8).toString()

            }
            if (formattedTime.subSequence(0,2).toString().equals("0:")){
                formattedTime = (formattedTime.subSequence(0,1).toString().toInt()+1).toString()+formattedTime.subSequence(2,8).toString()

            }


//            Log.d("Date ", "$formattedDate | $formattedTime")
        } catch (e: ParseException) {
            e.printStackTrace()
            Log.e("Error Date ", e.message!!)
        }
        return "$formattedDate | $formattedTime"
    }

    fun convertToGson(json: String): List<ArticleResponse>{
        val stringReader = StringReader(json)
        val jsonReader = JsonReader(stringReader)

        val gsonBuilder = GsonBuilder().serializeNulls()
        gsonBuilder.registerTypeAdapter(ArticleResponse::class.java, ArticleDeserialize())
        val gson = gsonBuilder.create()

        val articleList: List<ArticleResponse> = gson.fromJson(stringReader, Array<ArticleResponse>::class.java).toList()

        return articleList
    }

    fun convertToJson(list: List<ArticleResponse>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}