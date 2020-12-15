package com.gsanguinetti.reddittopposts.data.model.network


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubReddit(
    @SerialName("display_name_prefixed")
    val displayNamePrefixed: String,
    @SerialName("icon_img")
    val iconImg: String?
)