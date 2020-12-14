package com.gsanguinetti.reddittopposts.domain.repository

import androidx.paging.PagedList
import com.gsanguinetti.reddittopposts.domain.model.FetchStatus
import com.gsanguinetti.reddittopposts.domain.model.RedditPost
import io.reactivex.Observable

interface TopPostsRepository {
    fun getTopPosts(refreshData: Boolean): Observable<PagedList<RedditPost>>
    fun getNetworkFetchStatus(): Observable<FetchStatus>
}