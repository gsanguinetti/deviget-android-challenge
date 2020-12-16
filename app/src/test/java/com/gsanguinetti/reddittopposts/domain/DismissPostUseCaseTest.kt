package com.gsanguinetti.reddittopposts.domain

import com.gsanguinetti.reddittopposts.data.mapper.factory.DataModelsFactory
import com.gsanguinetti.reddittopposts.domain.repository.TopPostsRepository
import com.gsanguinetti.reddittopposts.domain.usecase.DismissPostUseCase
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DismissPostUseCaseTest {

    private lateinit var topPostsRepository: TopPostsRepository
    private lateinit var dismissPostUseCase: DismissPostUseCase

    @Before
    fun before() {
        topPostsRepository = mock()
        dismissPostUseCase = DismissPostUseCase(topPostsRepository)
    }

    @Test
    fun callUseCase() {
        val post = DataModelsFactory.makeRedditPost()

        //Simulate a correct response from Repository
        whenever(topPostsRepository.dismissPost(post)).thenReturn(Single.just(1))

        //Then verify
        dismissPostUseCase.buildUseCaseObservable(post)
            .test()
            .await()
            .assertNoErrors()
    }
}