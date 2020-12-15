package com.gsanguinetti.reddittopposts.data

import android.net.Uri
import androidx.paging.PagedList
import androidx.room.Room
import com.gsanguinetti.reddittopposts.R
import com.gsanguinetti.reddittopposts.base.data.NetworkApi
import com.gsanguinetti.reddittopposts.data.datasource.network.RedditNetworkDataSource
import com.gsanguinetti.reddittopposts.data.datasource.room.RedditLocalStorageDataSource
import com.gsanguinetti.reddittopposts.data.datasource.room.RedditPostsDatabase
import com.gsanguinetti.reddittopposts.data.mapper.RedditPostDomainMapper
import com.gsanguinetti.reddittopposts.data.mapper.SourcePostsMapper
import com.gsanguinetti.reddittopposts.data.model.PagingConfiguration
import com.gsanguinetti.reddittopposts.data.model.network.ServerEndpointConfiguration
import com.gsanguinetti.reddittopposts.data.repository.TopPostsNetworkToLocalDBRepository
import com.gsanguinetti.reddittopposts.domain.repository.TopPostsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    // Mapper injections
    factory { SourcePostsMapper() }
    factory { RedditPostDomainMapper() }

    // Networking injections
    single { PagingConfiguration(androidContext().resources.getInteger(R.integer.paging_size), androidContext().resources.getInteger(R.integer.client_post_limit)) }
    factory {
        ServerEndpointConfiguration(
            Uri.Builder().scheme(androidContext().getString(R.string.server_scheme))
                .authority(androidContext().getString(R.string.server_address)).build()
        )
    }
    single { NetworkApi(get(), androidContext()) }
    factory { RedditNetworkDataSource(get(), get()) }

    // Local Storage injections
    single {
        Room.databaseBuilder(
            androidContext(),
            RedditPostsDatabase::class.java,
            androidContext().getString(R.string.database_name)
        ).build()
    }
    factory {
        val pageSize = androidContext().resources.getInteger(R.integer.paging_size)
        PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setPrefetchDistance(androidContext().resources.getInteger(R.integer.paging_prefetch_distance))
            .setInitialLoadSizeHint(pageSize)
            .setEnablePlaceholders(false)
            .build()
    }
    factory { RedditLocalStorageDataSource(get(), get()) }
    single {
        TopPostsNetworkToLocalDBRepository(get(), get(), get(), get(), get()) as TopPostsRepository
    }
}