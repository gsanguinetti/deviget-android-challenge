package com.gsanguinetti.reddittopposts.data.datasource.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gsanguinetti.reddittopposts.data.model.local.PostLocalStatus
import com.gsanguinetti.reddittopposts.data.model.local.RedditLocalTopPost

@Dao
interface LocalPostStatusDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPostStatuses(posts: List<PostLocalStatus>)

    @Query("UPDATE posts_local_status SET read = :read WHERE postId = :id")
    fun setRead(id: String, read: Boolean)

    @Query("UPDATE posts_local_status SET hidden = :hidden WHERE postId = :id")
    fun setHidden(id: String, hidden: Boolean)

    @Query("UPDATE posts_local_status SET hidden = :hidden WHERE postId IN (:ids)")
    fun setHidden(ids: List<String>, hidden: Boolean)
}