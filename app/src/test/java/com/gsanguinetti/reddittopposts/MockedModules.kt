package com.gsanguinetti.reddittopposts

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.gsanguinetti.reddittopposts.data.datasource.room.RedditPostsDatabase
import org.koin.dsl.module

val mockedModules = listOf(
    module(override = true) {
        single {
            Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                RedditPostsDatabase::class.java
            ).build()
        }
    }
)