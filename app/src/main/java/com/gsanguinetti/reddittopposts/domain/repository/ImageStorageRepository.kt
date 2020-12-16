package com.gsanguinetti.reddittopposts.domain.repository

import android.net.Uri
import io.reactivex.Single

interface ImageStorageRepository {
    fun saveToStorage(imageUrl: String) :Single<Uri>
}