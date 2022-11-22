package com.andela.curreny.converter.base.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.andela.core.currencydb.DaoObj
import com.andela.core.currencydb.RoomDb
import com.andela.domain.entity.Conversion
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseRoomTest{

    private lateinit var database: RoomDb
    private lateinit var dao: DaoObj

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, RoomDb::class.java).build()
        dao = database.getConversion()

    }

    @Test
    fun wirteAndReadConversion() = runBlocking{
        val conversionTransaction = Conversion(System.currentTimeMillis(), "Dol", "Pound", 100.00)
        dao.insertSingleConversion(conversionTransaction)
        val transactions = dao.getHistory()
        assertTrue(transactions.contains(conversionTransaction))
    }

    @After
    fun closeDb(){
        database.close()
    }
}