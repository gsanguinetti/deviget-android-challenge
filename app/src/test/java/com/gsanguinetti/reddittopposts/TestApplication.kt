package com.gsanguinetti.reddittopposts

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.koin.core.module.Module

class TestApplication: RedditTopPostsApplication() {
    override fun getAppModules(): List<Module> {
        return appModules + mockedModules
    }
    override fun initHelpers() = Unit
    override fun getAppContext(): Context = ApplicationProvider.getApplicationContext()
}