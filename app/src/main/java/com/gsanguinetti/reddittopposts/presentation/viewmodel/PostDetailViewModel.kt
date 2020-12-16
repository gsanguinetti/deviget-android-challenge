package com.gsanguinetti.reddittopposts.presentation.viewmodel

import android.net.Uri
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.gsanguinetti.reddittopposts.base.presentation.SingleLiveEvent
import com.gsanguinetti.reddittopposts.base.presentation.UiEvent
import com.gsanguinetti.reddittopposts.domain.model.RedditPostDetails
import com.gsanguinetti.reddittopposts.domain.usecase.GetPostByIdUseCase
import com.gsanguinetti.reddittopposts.domain.usecase.SavePictureToStorageUseCase
import io.reactivex.observers.DisposableSingleObserver

class PostDetailViewModel(
    private val getPostByIdUseCase: GetPostByIdUseCase,
    private val savePictureToStorageUseCase: SavePictureToStorageUseCase
) : ViewModel() {

    private val postDetails = MutableLiveData<RedditPostDetails>()
    val showDetails = MutableLiveData<Boolean>()
    val title = Transformations.map(postDetails) { it.title }
    val imageUrl = Transformations.map(postDetails) { it.imageUrl }
    val hasImage = Transformations.map(postDetails) {
        !it.imageUrl.isNullOrEmpty() &&
                !Uri.parse(it.imageUrl).host.isNullOrEmpty()
    }
    val errorLoadingPostDetails = UiEvent()

    val savingPicture = MutableLiveData<Boolean>()
    val pictureSaved = SingleLiveEvent<Uri>()
    val pictureError = UiEvent()

    fun onPostSelected(id: String) {
        getPostByIdUseCase.execute(object : DisposableSingleObserver<RedditPostDetails>() {
            @WorkerThread
            override fun onSuccess(postDetails: RedditPostDetails) {
                this@PostDetailViewModel.postDetails.postValue(postDetails)
                showDetails.postValue(true)
            }

            @WorkerThread
            override fun onError(e: Throwable) {
                errorLoadingPostDetails.postCall()
                showDetails.postValue(false)
            }
        }, id)
    }

    fun onSaveImageToStorage() {
        postDetails.value?.imageUrl?.let {
            savePictureToStorageUseCase.execute(object : DisposableSingleObserver<Uri>() {
                override fun onStart() {
                    savingPicture.postValue(true)
                }

                override fun onSuccess(image: Uri) {
                    savingPicture.postValue(false)
                    pictureSaved.postValue(image)
                }

                override fun onError(e: Throwable) {
                    savingPicture.postValue(false)
                    pictureError.postCall()
                }
            }, it)
        }
    }

    fun notifyItemDismissed(id: String) {
        showDetails.postValue(id != postDetails.value?.id)
    }

    fun notifyItemsDismissed(ids: List<String>) {
        showDetails.postValue(!ids.contains(postDetails.value?.id))
    }
}