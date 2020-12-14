package com.gsanguinetti.reddittopposts.data.datasource.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gsanguinetti.reddittopposts.data.datasource.room.dao.LocalPostStatusDao
import com.gsanguinetti.reddittopposts.data.datasource.room.dao.LocalPostsDao
import com.gsanguinetti.reddittopposts.data.datasource.room.dao.PostsDao
import com.gsanguinetti.reddittopposts.data.model.local.PostLocalStatus
import com.gsanguinetti.reddittopposts.data.model.local.RedditLocalTopPost
import com.gsanguinetti.reddittopposts.data.model.local.RedditTopPost

@Database(
    entities = [PostLocalStatus::class, RedditTopPost::class],
    views = [RedditLocalTopPost::class],
    version = 1,
    exportSchema = false
)
abstract class RedditPostsDatabase : RoomDatabase() {
    abstract fun localPostsDao(): LocalPostsDao
    abstract fun postsDao(): PostsDao
    abstract fun localPostStatusDao(): LocalPostStatusDao
}