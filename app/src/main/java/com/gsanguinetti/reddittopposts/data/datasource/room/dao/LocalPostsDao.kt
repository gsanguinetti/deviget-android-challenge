package com.gsanguinetti.reddittopposts.data.datasource.room.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.gsanguinetti.reddittopposts.data.model.local.RedditLocalTopPost
import io.reactivex.Maybe

@Dao
interface LocalPostsDao {

    @Transaction
    @Query("SELECT * FROM RedditLocalTopPost")
    abstract fun getAllPosts(): DataSource.Factory<Int, RedditLocalTopPost>
}