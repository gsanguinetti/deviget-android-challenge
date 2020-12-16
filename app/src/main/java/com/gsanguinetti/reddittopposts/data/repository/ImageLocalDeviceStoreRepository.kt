package com.gsanguinetti.reddittopposts.data.repository

import android.content.Context
import com.gsanguinetti.reddittopposts.domain.repository.ImageStorageRepository

class ImageLocalDeviceStoreRepository(
    private val appContext: Context
): ImageStorageRepository {
    override fun saveToStorage(imageUrl: String) {

    }
}