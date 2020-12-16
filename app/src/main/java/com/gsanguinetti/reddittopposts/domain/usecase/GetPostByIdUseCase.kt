package com.gsanguinetti.reddittopposts.domain.usecase

import com.gsanguinetti.reddittopposts.base.domain.SingleUseCase
import com.gsanguinetti.reddittopposts.domain.model.RedditPostDetails
import com.gsanguinetti.reddittopposts.domain.repository.TopPostsRepository
import io.reactivex.Single

class GetPostByIdUseCase(
    private val topPostsRepository: TopPostsRepository
) : SingleUseCase<RedditPostDetails, String>() {

    override fun buildUseCaseObservable(params: String?): Single<RedditPostDetails> {
        checkNotNull(params)
        return topPostsRepository.getPost(params).setUpForUseCase()
    }
}