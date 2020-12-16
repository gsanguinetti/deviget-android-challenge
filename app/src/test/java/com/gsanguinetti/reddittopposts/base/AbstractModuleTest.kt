package com.gsanguinetti.reddittopposts.base

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.net.Uri
import androidx.room.RoomDatabase
import com.gsanguinetti.reddittopposts.data.datasource.room.RedditPostsDatabase
import com.nhaarman.mockitokotlin2.anyVararg
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest

abstract class AbstractModuleTest : AutoCloseKoinTest() {

    private val resources = mock<Resources> {
        on { getInteger(anyVararg()) } doReturn (randomInt())
        on { getDimension(anyVararg()) } doReturn (randomDouble().toFloat())
        on { getString(anyVararg()) } doReturn (randomString())
    }

    protected val mockedContextModule = listOf(
        module(override = true) {
            single {
                mock<Context> {
                    on { resources } doReturn (resources)
                    on { getString(anyVararg()) } doReturn ("")
                }
            }
            single {
                mock<Uri.Builder> {
                    on { scheme(anyVararg()) } doReturn (mock)
                    on { authority(anyVararg()) } doReturn (mock)
                    on { build() } doReturn (mock())
                }
            }
            single { mock<Application> {} }
            single { mock<RedditPostsDatabase> {} }
        }
    )
}