package com.andela.remote.datasource

import com.andela.remote.client.CurrencyApiClient
import com.andela.remote.models.CurrencyRates
import com.andela.remote.models.Symbols
import com.andela.utils.StateHandler
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class CurrencyRemoteDataSource @Inject constructor(
    private val currencyClientApi: CurrencyApiClient,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun getAllCurrencies(): StateHandler<Symbols> {
        return StateHandler.Success(currencyClientApi.getCountries())
    }

    suspend fun getLatestRates(base: String, conversion: String): StateHandler<CurrencyRates> {
        return StateHandler.Success(currencyClientApi.getLatestRates(base, conversion))
    }

}