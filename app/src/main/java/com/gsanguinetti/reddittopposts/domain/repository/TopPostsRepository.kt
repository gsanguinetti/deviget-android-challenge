package com.gsanguinetti.reddittopposts.domain.repository

import androidx.paging.PagedList
import com.gsanguinetti.reddittopposts.domain.model.FetchStatus
import com.gsanguinetti.reddittopposts.domain.model.RedditPost
import com.gsanguinetti.reddittopposts.domain.model.RedditPostDetails
import io.reactivex.Observable
import io.reactivex.Single

interface TopPostsRepository {
    fun getTopPosts(refreshData: Boolean): Observable<PagedList<RedditPost>>
    fun getNetworkFetchStatus(): Observable<FetchStatus>
    fun dismissPost(redditPost: RedditPost): Single<Int>
    fun dismissAll(ids: List<RedditPost>): Single<Int>
    fun setPostAsRead(redditPost: RedditPost): Single<Int>
    fun getPost(id: String): Single<RedditPostDetails>
}