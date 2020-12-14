package com.gsanguinetti.reddittopposts.domain.model

sealed class FetchStatus {
    object Loading : FetchStatus()
    object Done : FetchStatus()
    class Error(val throwable: Throwable) : FetchStatus()
}