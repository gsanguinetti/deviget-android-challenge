package com.gsanguinetti.reddittopposts.data.model.local

import androidx.room.DatabaseView

@DatabaseView("SELECT posts.id, posts.title, posts_local_status.read FROM posts " +
        "INNER JOIN posts_local_status ON postId = id WHERE posts_local_status.hidden = 0")
data class RedditLocalTopPost(
    val id: String,
    val title: String,
    val read: Boolean = false
)
