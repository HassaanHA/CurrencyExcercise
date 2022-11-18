package com.andela.remote.client

import com.andela.remote.models.CurrencyRates
import com.andela.remote.models.Symbols
import retrofit2.http.GET
import retrofit2.http.Query

public interface CurrencyApiClient {

    @GET("symbols")
    suspend fun getCountries(): Symbols

    @GET("latest")
    suspend fun getLatestRates(
        @Query("base") base: String,
        @Query("symbols") conversion: String): CurrencyRates
}