package com.gsanguinetti.reddittopposts.data.mapper.factory

import com.gsanguinetti.reddittopposts.base.randomBoolean
import com.gsanguinetti.reddittopposts.base.randomInt
import com.gsanguinetti.reddittopposts.base.randomLong
import com.gsanguinetti.reddittopposts.base.randomString
import com.gsanguinetti.reddittopposts.data.model.local.RedditTopPost
import com.gsanguinetti.reddittopposts.domain.model.RedditPost

class DataModelsFactory {
    companion object {
        fun makeRedditTopPost() :RedditTopPost =
            RedditTopPost(
                randomString(),
                randomString(),
                randomLong(),
                randomString(),
                randomString(),
                randomInt(),
                randomString(),
                randomString(),
                randomString(),
                randomLong(),
                randomInt()
            )

        fun makeRedditPost(): RedditPost =
            RedditPost(
                randomString(),
                randomString(),
                randomBoolean(),
                randomString(),
                randomString(),
                randomInt(),
                randomString(),
                randomString(),
                randomString(),
                randomLong()
            )
    }
}