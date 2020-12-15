package com.gsanguinetti.reddittopposts.domain

import com.gsanguinetti.reddittopposts.domain.usecase.*
import org.koin.dsl.module

val domainModule = module {
    factory { GetTopPostsUseCase(get()) }
    factory { GetPostFetchStatusUseCase(get()) }
    factory { DismissPostUseCase(get()) }
    factory { DismissAllUseCase(get()) }
    factory { SetPostAsReadUseCase(get()) }
}