package com.gsanguinetti.reddittopposts.presentation.ui.fragment

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.snackbar.Snackbar
import com.gsanguinetti.reddittopposts.R
import com.gsanguinetti.reddittopposts.base.presentation.showError
import com.gsanguinetti.reddittopposts.presentation.viewmodel.PostDetailViewModel
import kotlinx.android.synthetic.main.fragment_post_details.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PostDetailsFragment : Fragment() {

    companion object {
        private const val STORAGE_PERMISSION_REQUEST = 1002
    }

    private val postDetailsViewModel: PostDetailViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_post_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        saveThumbnailButton.setOnClickListener {
            activity?.let {
                if (checkSelfPermission(it, WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED)
                    requestPermissions(arrayOf(WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_REQUEST)
                else postDetailsViewModel.onSaveImageToStorage()
            }
        }

        postDetailsViewModel.showDetails.observe(this) {
            emptyDetailsLayout.visibility = if (it) View.GONE else View.VISIBLE
            contentLayout.visibility = if (!it) View.GONE else View.VISIBLE
        }

        postDetailsViewModel.title.observe(this) { postTitleTextView.text = it }
        postDetailsViewModel.hasImage.observe(this) {
            thumbnailImageView.visibility = if (!it) View.GONE else View.VISIBLE
            saveThumbnailButton.visibility = if (!it) View.GONE else View.VISIBLE
        }
        postDetailsViewModel.imageUrl.observe(this) {
            Glide.with(thumbnailImageView.context)
                .load(it)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(thumbnailImageView)
        }
        postDetailsViewModel.errorLoadingPostDetails.observe(this) {
            showError(R.string.error_selecting_post)
        }
        postDetailsViewModel.pictureError.observe(this) {
            showError(R.string.picture_saving_error)
            onSavePictureButtonState(false)
        }
        postDetailsViewModel.pictureSaved.observe(this) { uri ->
            onSavePictureButtonState(false)
            Snackbar.make(view, R.string.picture_saved, Snackbar.LENGTH_LONG)
                .setAction(R.string.open_picture) {
                    startActivity(Intent.createChooser(Intent().apply {
                        type = "image/jpg"
                        action = Intent.ACTION_VIEW
                        data = uri
                    }, getString(R.string.select_gallery_app)))
                }.show()

        }
        postDetailsViewModel.savingPicture.observe(this) {
            onSavePictureButtonState(true)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_REQUEST) {
            if (permissions[0] == WRITE_EXTERNAL_STORAGE && grantResults[0] == PERMISSION_GRANTED) {
                postDetailsViewModel.onSaveImageToStorage();
            } else {
                showError(R.string.permissions_denied)
            }
        }
    }

    private fun onSavePictureButtonState(loading: Boolean) {
        if (loading) {
            saveThumbnailButton.text = ""
            saveThumbnailButton.isEnabled = false
            savingPictureProgressBar.visibility = View.VISIBLE
        } else {
            saveThumbnailButton.text = getString(R.string.download_picture)
            saveThumbnailButton.isEnabled = true
            savingPictureProgressBar.visibility = View.GONE
        }
    }
}