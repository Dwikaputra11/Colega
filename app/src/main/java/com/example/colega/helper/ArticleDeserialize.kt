package com.example.colega.helper

import com.example.colega.models.news.ArticleResponse
import com.example.colega.models.news.ArticleSource
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.lang.reflect.Type

private const val TAG = "ArticleDeserialize"

class ArticleDeserialize: JsonDeserializer<ArticleResponse> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ArticleResponse {
        val jsonObj = json as JsonObject


        val jsonSource = jsonObj.getAsJsonObject("source")
        val id = if (jsonSource.get("id") == null) "" else jsonSource.get("id").asString
        val source = ArticleSource(
            id = id,
            name = jsonSource.get("name").asString,
        )


        val title = if (jsonObj.get("title") == null) "" else jsonObj.get("title").asString
        val author = if (jsonObj.get("author") == null) "" else jsonObj.get("author").asString
        val content = if (jsonObj.get("content") == null) "" else jsonObj.get("content").asString
        val description =
            if (jsonObj.get("description") == null) "" else jsonObj.get("description").asString
        val publishAt =
            if (jsonObj.get("publishAt") == null) "" else jsonObj.get("publishAt").asString
        val url = if (jsonObj.get("url") == null) "" else jsonObj.get("url").asString
        val urlToImage =
            if (jsonObj.get("urlToImage") == null) "" else jsonObj.get("urlToImage").asString


        return ArticleResponse(
            title = title,
            articleSource = source,
            author = author,
            content = content,
            description = description,
            publishedAt = publishAt,
            url = url,
            urlToImage = urlToImage,
            isCheck = jsonObj.get("isCheck").asBoolean
        )
    }
}