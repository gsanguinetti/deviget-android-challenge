package com.gsanguinetti.reddittopposts.presentation

import com.gsanguinetti.reddittopposts.presentation.viewmodel.PostListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { PostListViewModel(get(), get(), get(), get(), get()) }
}