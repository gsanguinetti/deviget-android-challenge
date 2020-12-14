package com.gsanguinetti.reddittopposts.data.mapper

import com.gsanguinetti.reddittopposts.base.data.DataMapper
import com.gsanguinetti.reddittopposts.data.model.local.RedditLocalTopPost
import com.gsanguinetti.reddittopposts.domain.model.RedditPost

class RedditPostDomainMapper :DataMapper<RedditLocalTopPost, RedditPost> {
    override fun mapFromEntity(entity: RedditLocalTopPost): RedditPost =
        RedditPost(
            entity.id,
            entity.title,
            entity.read
        )
}