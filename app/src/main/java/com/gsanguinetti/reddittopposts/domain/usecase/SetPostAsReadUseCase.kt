package com.gsanguinetti.reddittopposts.domain.usecase

import com.gsanguinetti.reddittopposts.base.domain.SingleUseCase
import com.gsanguinetti.reddittopposts.domain.model.RedditPost
import com.gsanguinetti.reddittopposts.domain.repository.TopPostsRepository
import io.reactivex.Single

class SetPostAsReadUseCase(
    private val topPostsRepository: TopPostsRepository
) : SingleUseCase<Unit, RedditPost>() {

    override fun buildUseCaseObservable(params: RedditPost?): Single<Unit> {
        checkNotNull(params)
        return topPostsRepository.setPostAsRead(params)
            .flatMap {
                if (it == 0) Single.error(IllegalAccessError("Repository has not set any post as read"))
                else Single.just(Unit)
            }.setUpForUseCase()
    }
}