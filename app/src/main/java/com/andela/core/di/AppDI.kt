package com.andela.core.di

import android.content.Context
import androidx.room.Room
import com.andela.core.currencydb.DaoObj
import com.andela.core.currencydb.RoomDb
import com.andela.data.remote.client.ApiClient
import com.andela.core.utils.CurrencyExcerciseInterceptor
import com.andela.core.utils.NetworkUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppDI {

    @Provides
    @Singleton
    fun provideInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    fun provideHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        currencyExcerciseInterceptor: CurrencyExcerciseInterceptor
    ): OkHttpClient.Builder =
        OkHttpClient.Builder().addInterceptor(loggingInterceptor).addInterceptor(currencyExcerciseInterceptor)

    @Provides
    @Singleton
    fun provideApiService(okHttpClient: OkHttpClient.Builder): ApiClient =
        Retrofit.Builder()
            .baseUrl(NetworkUtil.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient.build())
            .build()
            .create(ApiClient::class.java)

    @Singleton
    @Provides
    fun providesDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun getCurrenciesDB(@ApplicationContext context: Context) : DaoObj =
        Room.databaseBuilder(context, RoomDb::class.java,"CurrencyDB").build().getConversion()
}