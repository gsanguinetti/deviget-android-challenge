package com.gsanguinetti.reddittopposts.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class RedditTopPost(
    @PrimaryKey val id: String,
    val title: String
)