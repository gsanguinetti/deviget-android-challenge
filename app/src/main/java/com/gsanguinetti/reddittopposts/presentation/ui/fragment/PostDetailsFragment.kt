package com.gsanguinetti.reddittopposts.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.gsanguinetti.reddittopposts.R
import com.gsanguinetti.reddittopposts.base.presentation.showError
import com.gsanguinetti.reddittopposts.presentation.viewmodel.PostDetailViewModel
import kotlinx.android.synthetic.main.fragment_post_details.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PostDetailsFragment : Fragment() {

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
    }
}