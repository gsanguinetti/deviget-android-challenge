package com.gsanguinetti.reddittopposts.base.data

import android.content.Context
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.gsanguinetti.reddittopposts.BuildConfig
import com.gsanguinetti.reddittopposts.data.model.network.ServerEndpointConfiguration
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import io.reactivex.Single
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

class NetworkApi(
    private val serverEndpointConfiguration: ServerEndpointConfiguration,
    context: Context
) {

    private val retrofit = Retrofit.Builder()
        .baseUrl(serverEndpointConfiguration.address.toString())
        .addConverterFactory(
            Json {
                ignoreUnknownKeys = true
            }.asConverterFactory(MediaType.get("application/json"))
        )
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                this.addNetworkInterceptor(
                    FlipperOkhttpInterceptor(
                        AndroidFlipperClient.getInstance(context).getPluginByClass(
                            NetworkFlipperPlugin::class.java
                        )
                    )
                )
            }
        }.build())
        .build()

    fun <DATA : Any, T> makeApiCallForResponse(
        apiClass: Class<T>, apiCall: ((api: T) -> Single<DATA>)
    ): Single<DATA> =
        apiCall(retrofit.create(apiClass))
}