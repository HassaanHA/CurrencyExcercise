package com.andela.data.remote.client

import com.andela.data.remote.models.CurrencyRates
import com.andela.data.remote.models.Symbols
import retrofit2.http.GET
import retrofit2.http.Query

public interface ApiClient {

    @GET("symbols")
    suspend fun getCountries(): Symbols

    @GET("latest")
    suspend fun getLatestRates(
        @Query("base") base: String,
        @Query("symbols") conversion: String): CurrencyRates
}