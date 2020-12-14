package com.gsanguinetti.reddittopposts.domain.usecase

import com.gsanguinetti.reddittopposts.base.domain.ObservableUseCase
import com.gsanguinetti.reddittopposts.domain.model.FetchStatus
import com.gsanguinetti.reddittopposts.domain.repository.TopPostsRepository
import io.reactivex.Observable

class GetPostFetchStatusUseCase(
    private val topPostsRepository: TopPostsRepository
) : ObservableUseCase<FetchStatus, Unit>() {

    override fun buildUseCaseObservable(params: Unit?): Observable<FetchStatus> {
        return topPostsRepository.getNetworkFetchStatus().setUpForUseCase()
    }
}