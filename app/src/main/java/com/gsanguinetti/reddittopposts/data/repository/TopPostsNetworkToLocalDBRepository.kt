package com.gsanguinetti.reddittopposts.data.repository

import androidx.paging.PagedList
import com.gsanguinetti.reddittopposts.data.datasource.network.RedditNetworkDataSource
import com.gsanguinetti.reddittopposts.data.datasource.room.RedditLocalStorageDataSource
import com.gsanguinetti.reddittopposts.data.mapper.RedditPostDomainMapper
import com.gsanguinetti.reddittopposts.data.mapper.SourcePostsMapper
import com.gsanguinetti.reddittopposts.domain.model.FetchStatus
import com.gsanguinetti.reddittopposts.domain.model.RedditPost
import com.gsanguinetti.reddittopposts.domain.repository.TopPostsRepository
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class TopPostsNetworkToLocalDBRepository(
    private val postDomainMapper: RedditPostDomainMapper,
    private val networkSourcePostsMapper: SourcePostsMapper,
    private val networkDataSource: RedditNetworkDataSource,
    private val localStorageDataSource: RedditLocalStorageDataSource
) : TopPostsRepository, PagedList.BoundaryCallback<RedditPost>() {

    private var networkFetchRequest: Disposable? = null
    private var postStatusEmitter: ObservableEmitter<FetchStatus>? = null

    private var networkFetchStatusObserver = Observable.create<FetchStatus> {
        this.postStatusEmitter = it
    }

    override fun getTopPosts(refreshData: Boolean):
            Observable<PagedList<RedditPost>> {

        val prepareRequest =
            if (refreshData) localStorageDataSource.deleteAllPosts().toObservable()
            else Observable.just(Unit)

        return prepareRequest.flatMap {
            localStorageDataSource.getPosts(postDomainMapper, this)
        }
    }

    override fun getNetworkFetchStatus(): Observable<FetchStatus> = networkFetchStatusObserver

    override fun onZeroItemsLoaded() {
        feedDataFromNetwork()
    }

    override fun onItemAtFrontLoaded(itemAtFront: RedditPost) = Unit

    override fun onItemAtEndLoaded(itemAtEnd: RedditPost) {
        feedDataFromNetwork(itemAtEnd.id)
    }

    private fun feedDataFromNetwork(lastItemId: String? = null) {
        networkFetchRequest?.dispose()
        postStatusEmitter?.onNext(FetchStatus.Loading)
        networkFetchRequest = networkDataSource.getRedditTopPosts(lastItemId)
            .subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
            .subscribe({
                localStorageDataSource.addNewPosts(
                    it.responseData.children.map { networkSourcePostsMapper.mapFromEntity(it) }
                )
                postStatusEmitter?.onNext(FetchStatus.Done)
            }, {
                postStatusEmitter?.onNext(FetchStatus.Error(it))
            })
    }

}