package com.gsanguinetti.reddittopposts.domain.usecase

import com.gsanguinetti.reddittopposts.base.domain.SingleUseCase
import com.gsanguinetti.reddittopposts.domain.model.RedditPost
import com.gsanguinetti.reddittopposts.domain.repository.TopPostsRepository
import io.reactivex.Single

class DismissPostUseCase(
    private val topPostsRepository: TopPostsRepository
) : SingleUseCase<Unit, RedditPost>() {

    public override fun buildUseCaseObservable(params: RedditPost?): Single<Unit> {
        if(params == null) return Single.error(IllegalAccessError("Post param empty"))
        return topPostsRepository.dismissPost(params)
            .flatMap {
                if (it != 1) Single.error(IllegalAccessError("Repository has not dismissed any post"))
                else Single.just(Unit)
            }.setUpForUseCase()
    }
}