package com.gsanguinetti.reddittopposts.domain.model

sealed class FetchStatus {
    object LoadingInitial : FetchStatus()
    object LoadingNext : FetchStatus()
    object Done : FetchStatus()
    class Error(val throwable: Throwable) : FetchStatus()
}