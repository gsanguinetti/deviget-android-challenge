package com.gsanguinetti.reddittopposts.data.datasource.room

import androidx.paging.PagedList
import androidx.paging.toObservable
import com.gsanguinetti.reddittopposts.base.data.DataMapper
import com.gsanguinetti.reddittopposts.data.model.local.PostLocalStatus
import com.gsanguinetti.reddittopposts.data.model.local.RedditLocalTopPost
import com.gsanguinetti.reddittopposts.data.model.local.RedditTopPost
import io.reactivex.Observable
import io.reactivex.Single

class RedditLocalStorageDataSource(
    private val redditPostsDatabase: RedditPostsDatabase,
    private val pagingConfig: PagedList.Config
) {
    fun <T> getPosts(
        mapper: DataMapper<RedditLocalTopPost, T>,
        boundaryCallback: PagedList.BoundaryCallback<T>
    ):
            Observable<PagedList<T>> =
        redditPostsDatabase.localPostsDao()
            .getAllPosts()
            .map { mapper.mapFromEntity(it) }
            .toObservable(
                config = pagingConfig,
                boundaryCallback = boundaryCallback
            )

    fun getPostById(id: String): Single<RedditTopPost> =
        redditPostsDatabase.postsDao().getPostById(id)

    fun addNewStatuses(posts: List<RedditTopPost>): Single<List<Long>> {
        return redditPostsDatabase.localPostStatusDao()
            .insertPostStatuses(posts.map { PostLocalStatus(it.id) })
    }

    fun addNewPosts(posts: List<RedditTopPost>): Single<List<Long>> {
        return redditPostsDatabase.postsDao().insertPosts(posts)
    }

    fun deleteAllPosts(): Single<Int> = redditPostsDatabase.postsDao().deleteAllPosts()

    fun setPostAsRead(id: String): Single<Int> =
        redditPostsDatabase.localPostStatusDao().setRead(id, true)

    fun dismissPost(id: String): Single<Int> =
        redditPostsDatabase.localPostStatusDao().setHidden(id, true)

    fun dismissAll(ids: List<String>): Single<Int> =
        redditPostsDatabase.localPostStatusDao().setHidden(ids, true)
}