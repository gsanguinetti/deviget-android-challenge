package com.gsanguinetti.reddittopposts.data.model.network


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseData(
    @SerialName("after")
    val after: String? = null,
    @SerialName("before")
    val before: String? = null,
    @SerialName("children")
    val children: List<Post>,
    @SerialName("modhash")
    val modhash: String
)