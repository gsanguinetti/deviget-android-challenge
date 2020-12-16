package com.gsanguinetti.reddittopposts.data.repository

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.bumptech.glide.Glide
import com.gsanguinetti.reddittopposts.data.model.LocalImageSaveConfiguration
import com.gsanguinetti.reddittopposts.domain.repository.ImageStorageRepository
import io.reactivex.Single
import java.io.IOException
import java.io.OutputStream


class ImageLocalDeviceStoreRepository(
    private val appContext: Context,
    private val imageSaveConfiguration: LocalImageSaveConfiguration
) : ImageStorageRepository {

    override fun saveToStorage(imageUrl: String): Single<Uri> {
        return Single.create {
            try {
                val bitmap = Glide.with(appContext)
                    .asBitmap()
                    .load(imageUrl)
                    .submit()
                    .get()
                it.onSuccess(saveBitmap(bitmap))
            } catch (ex: Exception) {
                it.onError(ex)
            }
        }
    }

    private fun saveBitmap(image: Bitmap): Uri {
        val contentValues = ContentValues().apply {
            put(
                MediaStore.MediaColumns.DISPLAY_NAME,
                "${imageSaveConfiguration.fileNamePrefix}_${System.currentTimeMillis()}.jpg"
            )
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    "${Environment.DIRECTORY_PICTURES}/${imageSaveConfiguration.imageFolder}"
                )
            }
        }

        val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        var stream: OutputStream?
        var uri: Uri?

        val contentResolver = appContext.contentResolver
        uri = contentResolver.insert(contentUri, contentValues)
        if (uri == null) throw IOException("Failed to create new MediaStore record.")

        stream = contentResolver.openOutputStream(uri)

        if (stream == null) throw IOException("Failed to get output stream.")
        if (!image.compress(Bitmap.CompressFormat.JPEG, 100, stream))
            throw IOException("Failed to save bitmap.")

        stream.close()
        return uri
    }
}