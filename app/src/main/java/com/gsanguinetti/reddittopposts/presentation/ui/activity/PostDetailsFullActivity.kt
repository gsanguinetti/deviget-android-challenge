package com.gsanguinetti.reddittopposts.presentation.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.gsanguinetti.reddittopposts.R
import com.gsanguinetti.reddittopposts.presentation.ui.activity.PostDetailsFullActivity.Companion.POST_ID_EXTRA

fun Activity.launchPostDetailsFullActivity(postId: String) {
    startActivity(Intent(   this, PostDetailsFullActivity::class.java).apply {
        putExtra(POST_ID_EXTRA, postId)
    })
}

class PostDetailsFullActivity : AbstractPostDetailsActivity() {

    companion object {
        const val POST_ID_EXTRA = "PostIdExtra"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_post_details)

        check(intent.hasExtra(POST_ID_EXTRA))
        onItemSelected(intent.getStringExtra(POST_ID_EXTRA) ?: "")
    }
}