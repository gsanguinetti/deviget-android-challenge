package com.gsanguinetti.reddittopposts

import com.gsanguinetti.reddittopposts.data.dataModule
import com.gsanguinetti.reddittopposts.domain.domainModule
import com.gsanguinetti.reddittopposts.presentation.presentationModule

val appModules = listOf(
    presentationModule,
    domainModule,
    dataModule
)