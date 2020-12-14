package com.gsanguinetti.reddittopposts.data.model.network


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostData(
    @SerialName("author")
    val author: String,
    @SerialName("created_utc")
    val createdUtc: Double,
    @SerialName("id")
    val id: String,
    @SerialName("selftext")
    val selftext: String,
    @SerialName("subreddit")
    val subreddit: String,
    @SerialName("thumbnail")
    val thumbnail: String,
    @SerialName("title")
    val title: String,
    @SerialName("url")
    val url: String,
)