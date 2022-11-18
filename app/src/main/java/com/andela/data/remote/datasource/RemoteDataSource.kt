package com.andela.data.remote.datasource

import com.andela.data.remote.client.ApiClient
import com.andela.data.remote.models.CurrencyRates
import com.andela.data.remote.models.Symbols
import com.andela.utils.StateHandler
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val currencyClientApi: ApiClient,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun getAllCurrencies(): StateHandler<Symbols> {
        return StateHandler.Success(currencyClientApi.getCountries())
    }

    suspend fun getLatestRates(base: String, conversion: String): StateHandler<CurrencyRates> {
        return StateHandler.Success(currencyClientApi.getLatestRates(base, conversion))
    }

}