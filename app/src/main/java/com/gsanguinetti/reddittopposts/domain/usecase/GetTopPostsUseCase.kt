package com.gsanguinetti.reddittopposts.domain.usecase

import androidx.paging.PagedList
import com.gsanguinetti.reddittopposts.base.domain.ObservableUseCase
import com.gsanguinetti.reddittopposts.domain.model.RedditPost
import com.gsanguinetti.reddittopposts.domain.repository.TopPostsRepository
import io.reactivex.Observable

class GetTopPostsUseCase(
    private val topPostsRepository: TopPostsRepository
) : ObservableUseCase<PagedList<RedditPost>, Boolean>() {

    override fun buildUseCaseObservable(params: Boolean?): Observable<PagedList<RedditPost>> {
        return topPostsRepository.getTopPosts(params == true)
            .setUpForUseCase()
    }
}