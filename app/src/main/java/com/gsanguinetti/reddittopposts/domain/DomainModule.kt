package com.gsanguinetti.reddittopposts.domain

import com.gsanguinetti.reddittopposts.domain.usecase.GetPostFetchStatusUseCase
import com.gsanguinetti.reddittopposts.domain.usecase.GetTopPostsUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetTopPostsUseCase(get()) }
    factory { GetPostFetchStatusUseCase(get()) }
}