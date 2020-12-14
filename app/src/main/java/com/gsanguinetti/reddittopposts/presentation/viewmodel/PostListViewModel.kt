package com.gsanguinetti.reddittopposts.presentation.viewmodel

import androidx.annotation.WorkerThread
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.gsanguinetti.reddittopposts.base.presentation.UiEvent
import com.gsanguinetti.reddittopposts.domain.model.FetchStatus
import com.gsanguinetti.reddittopposts.domain.model.RedditPost
import com.gsanguinetti.reddittopposts.domain.usecase.GetPostFetchStatusUseCase
import com.gsanguinetti.reddittopposts.domain.usecase.GetTopPostsUseCase
import io.reactivex.observers.DisposableObserver

class PostListViewModel(
    private val getPostFetchStatusUseCase: GetPostFetchStatusUseCase,
    private val getTopPostsUseCase: GetTopPostsUseCase
) : ViewModel(), LifecycleObserver {

    val loadingData = MutableLiveData<Boolean>()
    val errorFetchingData = UiEvent()
    val postList = MutableLiveData<PagedList<RedditPost>>()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun onCreate() {
        getPostFetchStatusUseCase.execute(object :DisposableObserver<FetchStatus>() {
            @WorkerThread
            override fun onNext(status: FetchStatus) {
                loadingData.postValue(status is FetchStatus.Loading)
                errorFetchingData.postCall()
            }

            @WorkerThread
            override fun onError(e: Throwable) {
                loadingData.postValue(false)
                errorFetchingData.postCall()
            }

            @WorkerThread
            override fun onComplete() = Unit

        })

        onLoadData()
    }

    private fun onLoadData(refreshed :Boolean = false) {
        getTopPostsUseCase.execute(object :DisposableObserver<PagedList<RedditPost>>() {
            @WorkerThread
            override fun onNext(list: PagedList<RedditPost>) {
                postList.postValue(list)
            }

            @WorkerThread
            override fun onError(e: Throwable) {
                errorFetchingData.postCall()
            }

            @WorkerThread
            override fun onComplete() = Unit

        }, refreshed)
    }

    fun onRefreshData() {
        onLoadData(true)
    }
}