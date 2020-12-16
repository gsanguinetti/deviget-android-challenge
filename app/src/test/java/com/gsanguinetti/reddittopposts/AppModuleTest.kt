package com.gsanguinetti.reddittopposts

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.gsanguinetti.reddittopposts.base.AbstractModuleTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.test.check.checkModules
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28], application = Application::class)
class AppModuleTest : AbstractModuleTest() {

    lateinit var koinApplication: KoinApplication

    @Before
    fun before() {
        koinApplication = startKoin {
            modules(appModules + mockedContextModule)
            androidContext(ApplicationProvider.getApplicationContext())
        }
    }

    @Test
    fun check() = koinApplication.checkModules()
}