package com.gsanguinetti.reddittopposts.data.mapper

import com.gsanguinetti.reddittopposts.data.mapper.factory.DataModelsFactory
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class RedditPostDetailsDomainMapperTest {

    private lateinit var redditPostDetailsDomainMapper: RedditPostDetailsDomainMapper

    @Before
    fun before() {
        redditPostDetailsDomainMapper = RedditPostDetailsDomainMapper()
    }

    @Test
    fun mapFromRedditTopPost() {
        val topPost = DataModelsFactory.makeRedditTopPost()
        redditPostDetailsDomainMapper.mapFromEntity(topPost).run {
            assertEquals(id, topPost.id)
            assertEquals(title, topPost.title)
            assertEquals(imageUrl, topPost.thumbnail)
        }
    }
}