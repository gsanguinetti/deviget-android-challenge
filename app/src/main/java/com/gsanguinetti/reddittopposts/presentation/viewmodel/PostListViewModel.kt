package com.gsanguinetti.reddittopposts.presentation.viewmodel

import android.net.Uri
import androidx.annotation.WorkerThread
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.gsanguinetti.reddittopposts.base.presentation.SingleLiveEvent
import com.gsanguinetti.reddittopposts.base.presentation.UiEvent
import com.gsanguinetti.reddittopposts.domain.model.FetchStatus
import com.gsanguinetti.reddittopposts.domain.model.RedditPost
import com.gsanguinetti.reddittopposts.domain.usecase.*
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver

class PostListViewModel(
    private val getPostFetchStatusUseCase: GetPostFetchStatusUseCase,
    private val dismissPostUseCase: DismissPostUseCase,
    private val dismissAllUseCase: DismissAllUseCase,
    private val getTopPostsUseCase: GetTopPostsUseCase,
    private val setPostAsReadUseCase: SetPostAsReadUseCase
) : ViewModel() {

    val loadingInitial = MutableLiveData<Boolean>()
    val loadingNext = MutableLiveData<Boolean>()
    val errorFetchingData = UiEvent()
    val errorDismissingPost = UiEvent()
    val errorSelectingPost = UiEvent()
    val openUrlEvent = SingleLiveEvent<String>()
    val canDismissAll = MutableLiveData<Boolean>()
    val postList = MutableLiveData<PagedList<RedditPost>>()
    val itemDismissedEvent = SingleLiveEvent<String>()
    val itemsDismissedEvent = SingleLiveEvent<List<String>>()
    val itemSelectedEvent = SingleLiveEvent<String>()

    fun onCreate(refreshData: Boolean) {
        getPostFetchStatusUseCase.execute(object : DisposableObserver<FetchStatus>() {
            @WorkerThread
            override fun onNext(status: FetchStatus) {
                loadingInitial.postValue(status is FetchStatus.LoadingInitial)
                loadingNext.postValue(status is FetchStatus.LoadingNext)
                canDismissAll.postValue(status != FetchStatus.LoadingInitial)
                if(status is FetchStatus.Error) onErrorThrown()
            }

            @WorkerThread
            override fun onError(e: Throwable) {
                onErrorThrown()
            }

            @WorkerThread
            override fun onComplete() = Unit

        })

        onLoadData(refreshData)
    }

    private fun onLoadData(refreshData: Boolean) {
        getTopPostsUseCase.execute(object : DisposableObserver<PagedList<RedditPost>>() {
            @WorkerThread
            override fun onNext(list: PagedList<RedditPost>) {
                postList.postValue(list)
            }

            @WorkerThread
            override fun onError(e: Throwable) {
                onErrorThrown()
            }

            @WorkerThread
            override fun onComplete() = Unit

        }, refreshData)
    }

    private fun onErrorThrown() {
        loadingInitial.postValue(false)
        loadingNext.postValue(false)
        errorFetchingData.postCall()
        canDismissAll.postValue(false)
    }

    fun onRefreshData() {
        onLoadData(true)
    }

    fun onDismissPost(post: RedditPost) {
        dismissPostUseCase.execute(object : DisposableSingleObserver<Unit>() {
            override fun onSuccess(t: Unit) {
                itemDismissedEvent.postValue(post.id)
            }
            override fun onError(e: Throwable) {
                errorDismissingPost.postCall()
            }

        }, post)
    }

    fun onDismissingAll(postList: List<RedditPost>) {
        dismissAllUseCase.execute(object : DisposableSingleObserver<Unit>() {
            override fun onSuccess(t: Unit) {
                itemsDismissedEvent.postValue(postList.map { it.id })
            }
            override fun onError(e: Throwable) {
                errorDismissingPost.postCall()
            }

        }, postList)
    }

    fun onThumbnailClicked(post: RedditPost) {
        post.contentUrl?.let { openUrlEvent.postValue(it) }
    }

    fun isThumbnailClickable(post: RedditPost): Boolean{
        return !post.contentUrl.isNullOrEmpty() && !Uri.parse(post.contentUrl).host.isNullOrEmpty()
    }

    fun onPostSelected(post: RedditPost) {
        setPostAsReadUseCase.execute(object : DisposableSingleObserver<Unit>() {
            override fun onSuccess(t: Unit) = Unit
            override fun onError(e: Throwable) {
                errorSelectingPost.postCall()
            }

        }, post)
        itemSelectedEvent.postValue(post.id)
    }
}