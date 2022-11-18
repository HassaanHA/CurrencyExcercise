package com.andela.data.remote.datasource

import androidx.lifecycle.LiveData
import com.andela.currencydb.DaoObj
import com.andela.domain.entity.Conversion
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val currencyDao: DaoObj,
    private val coroutineDispatcher: CoroutineDispatcher
) {

    suspend fun saveTransactions(conversionTransaction: Conversion) =  withContext(coroutineDispatcher) {
        currencyDao.insertSingleConversion(conversionTransaction)
    }

    fun observeTransactions(): LiveData<List<Conversion>> {
        return currencyDao.observeTransactions()
    }

    suspend fun getHistory(): List<Conversion> {
        return currencyDao.getHistory()
    }

}