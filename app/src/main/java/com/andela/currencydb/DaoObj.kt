package com.andela.currencydb

import androidx.lifecycle.LiveData
import androidx.room.*
import com.andela.domain.entity.Conversion

@Dao
interface DaoObj {

    @Insert
    suspend fun insertAllConversions(conversionTransactions: List<Conversion>)

    @Insert
    suspend fun insertSingleConversion(conversionTransaction: Conversion)

    @Update
    fun singleConversion(conversionTransaction: Conversion)

    @Query("Select * from Conversion")
    fun observeTransactions(): LiveData<List<Conversion>>

    @Delete
    fun deleteConversion(conversionTransaction: Conversion)

    @Query("SELECT * from Conversion WHERE CAST((timeStamp / 1000) AS INTEGER) BETWEEN strftime('%s','now','-3 days') AND strftime('%s','now')  ORDER BY timeStamp DESC;")
    suspend fun getHistory() : List<Conversion>

}