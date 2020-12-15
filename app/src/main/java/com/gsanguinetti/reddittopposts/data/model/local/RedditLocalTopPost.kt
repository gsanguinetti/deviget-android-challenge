package com.gsanguinetti.reddittopposts.data.model.local

import androidx.room.DatabaseView

@DatabaseView(
    "SELECT posts.id, posts.title, posts_local_status.read, posts.author, posts.thumbnail, " +
            "posts.commentsCount, posts.contentUrl, posts.subredditName, posts.subRedditIcon, " +
            "posts.createdAt FROM posts INNER JOIN posts_local_status ON postId = id " +
            "WHERE posts_local_status.hidden = 0 ORDER BY posts.receivedTimestamp, posts.score DESC"
)
data class RedditLocalTopPost(
    val id: String,
    val title: String,
    val read: Boolean = false,
    val author: String,
    val thumbnail: String?,
    val commentsCount: Int,
    val contentUrl: String?,
    val subredditName: String,
    val subredditIcon: String?,
    val createdAt: Long
)
