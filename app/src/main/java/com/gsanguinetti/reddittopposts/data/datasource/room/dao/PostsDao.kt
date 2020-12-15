package com.gsanguinetti.reddittopposts.data.datasource.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gsanguinetti.reddittopposts.data.model.local.RedditTopPost
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface PostsDao {

    @Query("SELECT * FROM posts WHERE id = :id")
    fun getPostById(id: String): Single<RedditTopPost>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPosts(posts: List<RedditTopPost>): Single<List<Long>>

    @Query("DELETE FROM posts")
    fun deleteAllPosts(): Single<Int>
}