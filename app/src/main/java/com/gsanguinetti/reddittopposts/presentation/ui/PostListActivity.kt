package com.gsanguinetti.reddittopposts.presentation.ui

import android.content.res.Configuration
import android.os.Bundle
import com.gsanguinetti.reddittopposts.R
import com.gsanguinetti.reddittopposts.presentation.viewmodel.PostListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PostListActivity : AbstractPostDetailsActivity() {

    private val postListViewModel: PostListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_post_list)

        postListViewModel.onCreate(savedInstanceState == null)
        postListViewModel.itemDismissedEvent.observe(this) { onItemDismissed(it) }
        postListViewModel.itemsDismissedEvent.observe(this) { onItemsDismissed(it) }
        postListViewModel.itemSelectedEvent.observe(this) {
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
                Unit
            else onItemSelected(it)
        }
    }
}