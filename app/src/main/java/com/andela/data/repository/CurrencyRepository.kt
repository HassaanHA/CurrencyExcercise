package com.andela.data.repository

import androidx.lifecycle.LiveData
import com.andela.data.dto.convertToCurrencyModel
import com.andela.data.remote.datasource.LocalDataSource
import com.andela.data.remote.datasource.RemoteDataSource
import com.andela.domain.entity.Conversion
import com.andela.domain.entity.Currency
import com.andela.utils.StateHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val currencyRemoteDataSource: RemoteDataSource,
    private val currencyLocalDataSource: LocalDataSource,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun getAllCurrencies(): Flow<List<Currency>?> = flow {

            when (val response = currencyRemoteDataSource.getAllCurrencies()) {
                is StateHandler.Success -> {
                    emit(response.data.symbols?.convertToCurrencyModel())
                }
                is StateHandler.Loading -> emit(emptyList())
                is StateHandler.Failure -> emit(emptyList())
                is StateHandler.Empty -> emit(emptyList())
            }
    }.flowOn(dispatcher)

    suspend fun getConvertedCurrency(base: String, conversion: String): Flow<StateHandler<Conversion>> = flow {
        when(val response = currencyRemoteDataSource.getLatestRates(base, conversion)){
            is StateHandler.Success -> {
                val data =  response.data
                if(data.success == true) {
                    val convertedRate = data.rates?.get(conversion)
                    if(convertedRate != null) {
                        val transaction = Conversion(System.currentTimeMillis(), base, conversion, convertedRate)
                        currencyLocalDataSource.saveTransactions(transaction)
                        emit(StateHandler.Success(transaction))
                    }
                }
            }
            is StateHandler.Failure -> {
                emit(StateHandler.Failure(response.msg))
            }
            is StateHandler.Loading -> {
                emit(StateHandler.Loading)
            }
            is StateHandler.Empty -> {
                emit(StateHandler.Empty)
            }
        }

    }.flowOn(dispatcher)

    suspend fun getTopCurrency(base: String, conversion: String): Flow<StateHandler<List<Conversion>>> = flow {
        when(val response = currencyRemoteDataSource.getLatestRates(base, conversion)){
            is StateHandler.Success -> {
                val data =  response.data
                if(data.success == true) {
                    val list = mutableListOf<Conversion>()
                    data.rates?.forEach{
                        val convertedRate = it.value
                        val transaction = Conversion(System.currentTimeMillis(), base, it.key, convertedRate)
                        list.add(transaction)
                    }
                    emit(StateHandler.Success(list))
                }
            }
            is StateHandler.Failure -> {
                emit(StateHandler.Failure(response.msg))
            }
            is StateHandler.Loading -> {
                emit(StateHandler.Loading)
            }
            is StateHandler.Empty -> {
                emit(StateHandler.Empty)
            }
        }

    }.flowOn(dispatcher)

    fun observeTransactions(): LiveData<List<Conversion>> {
        return currencyLocalDataSource.observeTransactions()
    }

    suspend fun getHistory(): List<Conversion> {
        return currencyLocalDataSource.getHistory()
    }
}