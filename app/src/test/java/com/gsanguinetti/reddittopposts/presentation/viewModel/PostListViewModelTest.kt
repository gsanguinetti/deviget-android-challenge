package com.gsanguinetti.reddittopposts.presentation.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagedList
import com.gsanguinetti.reddittopposts.data.mapper.factory.DataModelsFactory.Companion.makeRedditPost
import com.gsanguinetti.reddittopposts.domain.model.FetchStatus
import com.gsanguinetti.reddittopposts.domain.model.RedditPost
import com.gsanguinetti.reddittopposts.domain.usecase.*
import com.gsanguinetti.reddittopposts.presentation.viewmodel.PostListViewModel
import com.nhaarman.mockitokotlin2.*
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class PostListViewModelTest {
    private lateinit var postListViewModel: PostListViewModel

    private lateinit var getPostFetchStatusUseCase: GetPostFetchStatusUseCase
    private lateinit var dismissPostUseCase: DismissPostUseCase
    private lateinit var dismissAllUseCase: DismissAllUseCase
    private lateinit var getTopPostsUseCase: GetTopPostsUseCase
    private lateinit var setPostAsReadUseCase: SetPostAsReadUseCase

    private lateinit var fetchStatusExecutorCaptor: KArgumentCaptor<DisposableObserver<FetchStatus>>
    private lateinit var dismissPostExecutorCaptor: KArgumentCaptor<DisposableSingleObserver<Unit>>
    private lateinit var dismissAllExecutorCaptor: KArgumentCaptor<DisposableSingleObserver<Unit>>
    private lateinit var topPostsExecutorCaptor: KArgumentCaptor<DisposableObserver<PagedList<RedditPost>>>
    private lateinit var postAsReadExecutorCaptor: KArgumentCaptor<DisposableSingleObserver<Unit>>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun before() {
        getPostFetchStatusUseCase = mock()
        dismissPostUseCase = mock()
        dismissAllUseCase = mock()
        getTopPostsUseCase = mock()
        setPostAsReadUseCase = mock()

        postListViewModel = PostListViewModel(
            getPostFetchStatusUseCase,
            dismissPostUseCase,
            dismissAllUseCase,
            getTopPostsUseCase,
            setPostAsReadUseCase
        )

        fetchStatusExecutorCaptor = argumentCaptor()
        dismissAllExecutorCaptor = argumentCaptor()
        dismissPostExecutorCaptor = argumentCaptor()
        topPostsExecutorCaptor = argumentCaptor()
        postAsReadExecutorCaptor = argumentCaptor()
    }

    @Test
    fun callOnCreateTest() {
        // Call on create
        postListViewModel.onCreate(true)
        // Must get the fetch status
        verify(getPostFetchStatusUseCase).execute(fetchStatusExecutorCaptor.capture(), eq(null))
        // And as we set refreshData = true, it must call get top posts use case
        verify(getTopPostsUseCase).execute(topPostsExecutorCaptor.capture(), eq(true))

        // Simulate response, start loading posts
        fetchStatusExecutorCaptor.firstValue.onNext(FetchStatus.LoadingInitial)
        assert(postListViewModel.loadingInitial.value == true)
        assert(postListViewModel.loadingNext.value != true)

        // Loading page
        fetchStatusExecutorCaptor.firstValue.onNext(FetchStatus.LoadingNext)
        assert(postListViewModel.loadingInitial.value != true)
        assert(postListViewModel.loadingNext.value == true)

        // Done loading
        fetchStatusExecutorCaptor.firstValue.onNext(FetchStatus.Done)
        assert(postListViewModel.loadingInitial.value != true)
        assert(postListViewModel.loadingNext.value != true)


        // Simulate a paging response coming from the use case
        val response = mock<PagedList<RedditPost>>()
        topPostsExecutorCaptor.firstValue.onNext(response)
        assert(postListViewModel.postList.value == response)
    }

    @Test
    fun callDismissOnePost() {
        val post = makeRedditPost()

        // Simulate dismissing a post
        postListViewModel.onDismissPost(post)

        // Must start the use case
        verify(dismissPostUseCase).execute(dismissPostExecutorCaptor.capture(), eq(post))

        // Simulate the correct response
        dismissPostExecutorCaptor.firstValue.onSuccess(Unit)

        // Verify
        assert(postListViewModel.itemDismissedEvent.value == post.id)
    }

    @Test
    fun callDismissAllPosts() {
        val posts = listOf(makeRedditPost(), makeRedditPost())

        // Simulate dismissing posts
        postListViewModel.onDismissingAll(posts)

        // Must start the use case
        verify(dismissAllUseCase).execute(dismissAllExecutorCaptor.capture(), eq(posts))

        // Simulate the correct response
        dismissAllExecutorCaptor.firstValue.onSuccess(Unit)

        // Verify
        assert(postListViewModel.itemsDismissedEvent.value == posts.map { it.id })
    }

    @Test
    fun callMarkPostAsRead() {
        val post = makeRedditPost()

        // Simulate dismissing a post
        postListViewModel.onPostSelected(post)

        // Must start the use case
        verify(setPostAsReadUseCase).execute(postAsReadExecutorCaptor.capture(), eq(post))
    }

}