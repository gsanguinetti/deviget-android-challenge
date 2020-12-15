package com.gsanguinetti.reddittopposts.data.datasource.network

import com.gsanguinetti.reddittopposts.data.model.network.RedditTopResponse
import com.gsanguinetti.reddittopposts.data.model.network.ResponseData
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RedditService {

    @GET("/top")
    fun top(
        @Query("after") after: String? = null,
        @Query("before") before: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("count") count: Int? = null,
        @Query("show") show: String? = null,
        @Query("sr_detail") subredditDetail: Boolean = true
    ): Single<RedditTopResponse>
}