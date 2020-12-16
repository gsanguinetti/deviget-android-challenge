package com.gsanguinetti.reddittopposts.data

import android.net.Uri
import androidx.paging.PagedList
import androidx.room.Room
import com.gsanguinetti.reddittopposts.R
import com.gsanguinetti.reddittopposts.base.data.NetworkApi
import com.gsanguinetti.reddittopposts.data.datasource.network.RedditNetworkDataSource
import com.gsanguinetti.reddittopposts.data.datasource.room.RedditLocalStorageDataSource
import com.gsanguinetti.reddittopposts.data.datasource.room.RedditPostsDatabase
import com.gsanguinetti.reddittopposts.data.mapper.RedditPostDetailsDomainMapper
import com.gsanguinetti.reddittopposts.data.mapper.RedditPostDomainMapper
import com.gsanguinetti.reddittopposts.data.mapper.SourcePostsMapper
import com.gsanguinetti.reddittopposts.data.model.LocalImageSaveConfiguration
import com.gsanguinetti.reddittopposts.data.model.PagingConfiguration
import com.gsanguinetti.reddittopposts.data.model.network.ServerEndpointConfiguration
import com.gsanguinetti.reddittopposts.data.repository.ImageLocalDeviceStoreRepository
import com.gsanguinetti.reddittopposts.data.repository.TopPostsNetworkToLocalDBRepository
import com.gsanguinetti.reddittopposts.domain.repository.ImageStorageRepository
import com.gsanguinetti.reddittopposts.domain.repository.TopPostsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    // Mapper injections
    factory { SourcePostsMapper() }
    factory { RedditPostDomainMapper() }
    factory { RedditPostDetailsDomainMapper() }

    // Networking injections
    single {
        PagingConfiguration(
            androidContext().resources.getInteger(R.integer.paging_size),
            androidContext().resources.getInteger(R.integer.client_post_limit)
        )
    }
    factory {
        ServerEndpointConfiguration(
            get<Uri.Builder>().scheme(androidContext().getString(R.string.server_scheme))
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
        TopPostsNetworkToLocalDBRepository(
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        ) as TopPostsRepository
    }
    single { ImageLocalDeviceStoreRepository(androidContext(), get()) as ImageStorageRepository }
    single {
        LocalImageSaveConfiguration(
            androidContext().getString(R.string.image_prefix),
            androidContext().getString(R.string.local_image_store_folder)
        )
    }
    factory { Uri.Builder() }
}