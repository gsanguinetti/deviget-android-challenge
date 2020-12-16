package com.gsanguinetti.reddittopposts.domain.repository

interface ImageStorageRepository {
    fun saveToStorage(imageUrl: String)
}