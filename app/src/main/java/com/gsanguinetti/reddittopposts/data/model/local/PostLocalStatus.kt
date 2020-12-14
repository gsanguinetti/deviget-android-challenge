package com.gsanguinetti.reddittopposts.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "posts_local_status"
)
data class PostLocalStatus(
    @PrimaryKey val postId: String,
    val hidden: Boolean = false,
    val read: Boolean = false
)
