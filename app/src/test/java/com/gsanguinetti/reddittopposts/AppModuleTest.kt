package com.gsanguinetti.reddittopposts

import com.gsanguinetti.reddittopposts.base.AbstractModuleTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.test.check.checkModules

@RunWith(JUnit4::class)
class AppModuleTest : AbstractModuleTest() {

    lateinit var koinApplication: KoinApplication

    @Before
    fun before() {
        koinApplication = startKoin { modules(appModules + mockedContextModule) }
    }

    @Test
    fun check() = koinApplication.checkModules()
}