package com.gsanguinetti.reddittopposts.data.datasource.network

import com.gsanguinetti.reddittopposts.base.data.NetworkApi
import com.gsanguinetti.reddittopposts.data.model.PagingConfiguration
import com.gsanguinetti.reddittopposts.data.model.network.RedditTopResponse
import com.gsanguinetti.reddittopposts.data.model.network.ResponseData
import io.reactivex.Single

class RedditNetworkDataSource(
    private val api: NetworkApi,
    private val pagingConfiguration: PagingConfiguration
) {
    fun getRedditTopPosts(
        lastItemKey: String? = null
    ): Single<RedditTopResponse> =
        api.makeApiCallForResponse(RedditService::class.java) {
            it.top(after = lastItemKey, limit = pagingConfiguration.pageSize)
        }
}