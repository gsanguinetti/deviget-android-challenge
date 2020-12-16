package com.gsanguinetti.reddittopposts

import android.app.Application
import android.content.Context
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.soloader.SoLoader
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.Module


open class RedditTopPostsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(getAppContext())
            modules(getAppModules())
        }
        initHelpers()
    }

    protected open fun getAppModules(): List<Module> = appModules

    protected open fun initHelpers() {
        SoLoader.init(this, false)

        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(this)) {
            AndroidFlipperClient.getInstance(this).run {
                addPlugin(
                    InspectorFlipperPlugin(
                        this@RedditTopPostsApplication,
                        DescriptorMapping.withDefaults()
                    )
                )
                addPlugin(DatabasesFlipperPlugin(this@RedditTopPostsApplication))
                addPlugin(NetworkFlipperPlugin())
                start()
            }
        }
    }

    protected open fun getAppContext(): Context = this
}