package com.gsanguinetti.reddittopposts.data.mapper

import com.gsanguinetti.reddittopposts.base.data.DataMapper
import com.gsanguinetti.reddittopposts.data.model.local.RedditTopPost
import com.gsanguinetti.reddittopposts.domain.model.RedditPostDetails

class RedditPostDetailsDomainMapper : DataMapper<RedditTopPost, RedditPostDetails> {
    override fun mapFromEntity(entity: RedditTopPost): RedditPostDetails =
        RedditPostDetails(
            entity.id,
            entity.title,
            entity.thumbnail,
        )
}