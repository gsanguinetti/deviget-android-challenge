package com.gsanguinetti.reddittopposts.data.repository

import androidx.paging.PagedList
import com.gsanguinetti.reddittopposts.data.datasource.network.RedditNetworkDataSource
import com.gsanguinetti.reddittopposts.data.datasource.room.RedditLocalStorageDataSource
import com.gsanguinetti.reddittopposts.data.mapper.RedditPostDetailsDomainMapper
import com.gsanguinetti.reddittopposts.data.mapper.RedditPostDomainMapper
import com.gsanguinetti.reddittopposts.data.mapper.SourcePostsMapper
import com.gsanguinetti.reddittopposts.data.model.PagingConfiguration
import com.gsanguinetti.reddittopposts.data.model.local.RedditTopPost
import com.gsanguinetti.reddittopposts.domain.model.FetchStatus
import com.gsanguinetti.reddittopposts.domain.model.RedditPost
import com.gsanguinetti.reddittopposts.domain.model.RedditPostDetails
import com.gsanguinetti.reddittopposts.domain.repository.TopPostsRepository
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class TopPostsNetworkToLocalDBRepository(
    private val postDomainMapper: RedditPostDomainMapper,
    private val postDetailsMapper: RedditPostDetailsDomainMapper,
    private val networkSourcePostsMapper: SourcePostsMapper,
    private val networkDataSource: RedditNetworkDataSource,
    private val localStorageDataSource: RedditLocalStorageDataSource,
    private val pagingConfiguration: PagingConfiguration
) : TopPostsRepository, PagedList.BoundaryCallback<RedditPost>() {

    private var networkFetchRequest: Disposable? = null
    private var postStatusEmitter: ObservableEmitter<FetchStatus>? = null

    private var lastItemId: String? = null
    private var topPostsLoadedFromNetwork = 0
    private var itemAtFrontLoaded = false

    private var networkFetchStatusObserver = Observable.create<FetchStatus> {
        this.postStatusEmitter = it
    }

    override fun getTopPosts(refreshData: Boolean):
            Observable<PagedList<RedditPost>> {
        if (refreshData) {
            topPostsLoadedFromNetwork = 0
            lastItemId = null
            feedDataFromNetwork()
        }
        return localStorageDataSource.getPosts(postDomainMapper, this)
    }

    override fun getNetworkFetchStatus(): Observable<FetchStatus> = networkFetchStatusObserver

    override fun onZeroItemsLoaded() {
        itemAtFrontLoaded = false
        feedDataFromNetwork()
    }

    override fun onItemAtFrontLoaded(itemAtFront: RedditPost) {
        itemAtFrontLoaded = true
        postStatusEmitter?.onNext(FetchStatus.Done)
    }

    override fun onItemAtEndLoaded(itemAtEnd: RedditPost) {
        lastItemId = itemAtEnd.id
        feedDataFromNetwork()
    }

    private fun feedDataFromNetwork() {
        if (topPostsLoadedFromNetwork < pagingConfiguration.postsLimit) {
            var posts: List<RedditTopPost>? = null

            networkFetchRequest?.dispose()
            postStatusEmitter?.onNext(
                if (!itemAtFrontLoaded) FetchStatus.LoadingInitial else FetchStatus.LoadingNext
            )
            networkFetchRequest = networkDataSource.getRedditTopPosts(lastItemId)
                .flatMap {
                    posts =
                        it.responseData.children.map { networkSourcePostsMapper.mapFromEntity(it) }
                    if (lastItemId == null) localStorageDataSource.deleteAllPosts()
                    else Single.just(Unit)
                }.flatMap { localStorageDataSource.addNewPosts(posts ?: listOf()) }
                .flatMap {
                    if (posts != null) localStorageDataSource.addNewStatuses(posts!!)
                    else Single.just(Unit)
                }
                .subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                .subscribe({
                    topPostsLoadedFromNetwork += posts?.size ?: 0
                    if (!posts.isNullOrEmpty()) lastItemId = posts!!.last().id
                }, {
                    postStatusEmitter?.onNext(FetchStatus.Error(it))
                })
        } else postStatusEmitter?.onNext(FetchStatus.Done)
    }

    override fun dismissPost(redditPost: RedditPost): Single<Int> {
        return localStorageDataSource.dismissPost(redditPost.id)
    }

    override fun dismissAll(redditPosts: List<RedditPost>): Single<Int> {
        if (redditPosts.isNotEmpty()) lastItemId = redditPosts.last().id
        return localStorageDataSource.dismissAll(redditPosts.map { it.id })
    }

    override fun setPostAsRead(redditPost: RedditPost): Single<Int> {
        return localStorageDataSource.setPostAsRead(redditPost.id)
    }

    override fun getPost(id: String): Single<RedditPostDetails> {
        return localStorageDataSource.getPostById(id)
            .map { postDetailsMapper.mapFromEntity(it) }
    }
}