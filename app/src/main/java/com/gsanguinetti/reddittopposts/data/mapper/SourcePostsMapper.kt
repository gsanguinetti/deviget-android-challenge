package com.gsanguinetti.reddittopposts.data.mapper

import com.gsanguinetti.reddittopposts.base.data.DataMapper
import com.gsanguinetti.reddittopposts.data.model.local.RedditTopPost
import com.gsanguinetti.reddittopposts.data.model.network.Post

class SourcePostsMapper :DataMapper<Post, RedditTopPost> {
    override fun mapFromEntity(entity: Post): RedditTopPost =
        RedditTopPost(
            "${entity.kind}_${entity.postData.id}",
            entity.postData.title
        )
}