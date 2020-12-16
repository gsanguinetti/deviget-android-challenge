package com.gsanguinetti.reddittopposts.domain.usecase

import android.net.Uri
import com.gsanguinetti.reddittopposts.base.domain.SingleUseCase
import com.gsanguinetti.reddittopposts.domain.repository.ImageStorageRepository
import io.reactivex.Single

class SavePictureToStorageUseCase(
    private val imageStorageRepository: ImageStorageRepository
): SingleUseCase<Uri, String>() {
    override fun buildUseCaseObservable(params: String?): Single<Uri> {
        if(params.isNullOrEmpty()) return Single.error(IllegalAccessError("Filename empty"))
        return imageStorageRepository.saveToStorage(params)
    }
}