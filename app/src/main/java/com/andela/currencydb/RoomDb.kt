package com.andela.currencydb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.andela.domain.entity.Conversion

@Database(
  entities = [Conversion::class],
  version = 1,
  exportSchema = false
)
abstract class RoomDb : RoomDatabase() {
    abstract fun getConversion(): DaoObj
}