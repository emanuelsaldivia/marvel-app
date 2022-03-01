package com.esaldivia.marvelheroes.di

import com.esaldivia.marvelheroes.BuildConfig
import com.esaldivia.marvelheroes.network.CharacterNetworkService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import toothpick.config.Module
import toothpick.ktp.binding.bind
import java.util.concurrent.TimeUnit

class NetworkModule : Module() {

    init {
        val retrofit = getRetrofitInstance()
        bind<Retrofit>().toInstance(retrofit)
        bind<CharacterNetworkService>().toInstance(retrofit.create(CharacterNetworkService::class.java))
    }

    private fun getRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(getInterceptor())
                    .connectTimeout(60, TimeUnit.SECONDS).build()
            )
            .build()
    }

    private fun getInterceptor() = Interceptor { chain ->
        val request = chain.request()
            .newBuilder()
            .url(
                chain.request()
                    .url
                    .newBuilder()
                    .addQueryParameter("apikey", BuildConfig.apikey)
                    .addQueryParameter("hash", BuildConfig.hash)
                    .build()
            ).build()
        return@Interceptor chain.proceed(request)
    }

}