package com.gsanguinetti.reddittopposts.presentation.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.gsanguinetti.reddittopposts.presentation.viewmodel.PostDetailViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class AbstractPostDetailsActivity : AppCompatActivity() {

    private val postDetailsViewModel: PostDetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    fun onItemDismissed(id: String) {
        postDetailsViewModel.notifyItemDismissed(id)
    }

    fun onItemsDismissed(ids: List<String>) {
        postDetailsViewModel.notifyItemsDismissed(ids)
    }

    fun onItemSelected(id: String) {
        postDetailsViewModel.onPostSelected(id)
    }
}