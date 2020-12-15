package com.gsanguinetti.reddittopposts.domain.usecase

import com.gsanguinetti.reddittopposts.base.domain.SingleUseCase
import com.gsanguinetti.reddittopposts.domain.model.RedditPost
import com.gsanguinetti.reddittopposts.domain.repository.TopPostsRepository
import io.reactivex.Single

class DismissAllUseCase(
    private val topPostsRepository: TopPostsRepository
) : SingleUseCase<Unit, List<RedditPost>>() {

    override fun buildUseCaseObservable(params: List<RedditPost>?): Single<Unit> {
        checkNotNull(params)
        return topPostsRepository.dismissAll(params)
            .flatMap {
                if (it == 0) Single.error(IllegalAccessError("Repository has not dismissed any post"))
                else Single.just(Unit)
            }.setUpForUseCase()
    }
}