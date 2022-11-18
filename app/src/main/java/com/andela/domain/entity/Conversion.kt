package com.andela.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Conversion(

    @PrimaryKey
    @ColumnInfo(name = "timeStamp")
    val timestamp: Long,

    @ColumnInfo(name = "baseCurrency")
    val baseCurrency: String,

    @ColumnInfo(name = "convertedCurrency")
    val convertedCurrency: String,

    @ColumnInfo(name = "rate")
    val rate: Double
)