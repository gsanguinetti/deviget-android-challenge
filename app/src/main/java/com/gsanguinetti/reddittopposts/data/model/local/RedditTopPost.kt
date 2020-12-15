package com.gsanguinetti.reddittopposts.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(tableName = "posts")
data class RedditTopPost(
    @PrimaryKey val id: String,
    val title: String,
    val receivedTimestamp: Long,
    val author: String,
    val thumbnail: String?,
    val commentsCount: Int,
    val contentUrl: String?,
    val subredditName: String,
    val subredditIcon: String?,
    val createdAt: Long,
    val score: Int
)