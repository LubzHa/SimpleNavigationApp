package com.example.navigationapp.di

import com.example.navigationapp.data.remote.DirectionsAPI
import com.example.navigationapp.data.repository.DirectionsRepository
import com.example.navigationapp.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDirectionsRepository(
        api: DirectionsAPI
    ) = DirectionsRepository(api)

    @Singleton
    @Provides
    fun provideDirectionsAPI(): DirectionsAPI {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DirectionsAPI::class.java)
    }
}