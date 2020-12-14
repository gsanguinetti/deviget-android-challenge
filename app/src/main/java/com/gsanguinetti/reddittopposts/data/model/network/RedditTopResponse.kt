package com.gsanguinetti.reddittopposts.data.model.network


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RedditTopResponse(
    @SerialName("data")
    val responseData: ResponseData,
    @SerialName("kind")
    val kind: String
)