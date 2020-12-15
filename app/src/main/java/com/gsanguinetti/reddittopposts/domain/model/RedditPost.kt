package com.gsanguinetti.reddittopposts.domain.model

data class RedditPost(
    val id: String,
    val title: String,
    val read: Boolean,
    val author: String,
    val thumbnail: String?,
    val commentsCount: Int,
    val contentUrl: String?,
    val subredditName: String,
    val subredditIcon: String?,
    val createdAt: Long
)