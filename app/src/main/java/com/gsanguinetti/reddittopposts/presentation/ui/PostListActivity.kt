package com.gsanguinetti.reddittopposts.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.gsanguinetti.reddittopposts.R
import com.gsanguinetti.reddittopposts.presentation.viewmodel.PostListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PostListActivity : AppCompatActivity() {

    private val postListViewModel: PostListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycle.addObserver(postListViewModel)

        postListViewModel.loadingData.observe(this) {
            Log.i(this::class.java.simpleName, "loadingData: $it")
        }

        postListViewModel.errorFetchingData.observe(this) {
            Log.i(this::class.java.simpleName, "errorFetchingData: $it")
        }

        postListViewModel.postList.observe(this) {
            Log.i(this::class.java.simpleName, "postList: $it")
        }
    }
}