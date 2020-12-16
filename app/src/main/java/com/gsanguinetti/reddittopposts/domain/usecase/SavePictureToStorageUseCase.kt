package com.gsanguinetti.reddittopposts.domain.usecase

import com.gsanguinetti.reddittopposts.base.domain.SingleUseCase
import com.gsanguinetti.reddittopposts.domain.repository.ImageStorageRepository
import io.reactivex.Single

class SavePictureToStorageUseCase(
    private val imageStorageRepository: ImageStorageRepository
): SingleUseCase<String, String>() {
    override fun buildUseCaseObservable(params: String?): Single<String> {
        TODO("Not yet implemented")
    }

}