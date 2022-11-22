package com.andela.curreny.converter.presentation.viewModels

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.andela.core.currencydb.RoomDb
import com.andela.data.remote.client.ApiClient
import com.andela.data.remote.datasource.LocalDataSource
import com.andela.data.remote.datasource.RemoteDataSource
import com.andela.data.repository.CurrencyRepository
import com.andela.domain.entity.Conversion
import com.andela.presentation.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@AndroidEntryPoint
class MainViewModelTest {

    @Inject
    private lateinit var currencyClientApi: ApiClient

    private lateinit var viewModel: MainViewModel
    private lateinit var localDataSource: LocalDataSource

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = Room.inMemoryDatabaseBuilder(context, RoomDb::class.java)
            .allowMainThreadQueries().build()
        localDataSource = LocalDataSource(db.getConversion(), Dispatchers.IO)
        val remoteDataSource = RemoteDataSource(currencyClientApi, Dispatchers.IO)
        val repository = CurrencyRepository(remoteDataSource, localDataSource, Dispatchers.IO)
        viewModel = MainViewModel(repository)
    }

    @Test
    fun testTransactionViewModel() = runBlocking{
        val conversionTransaction = Conversion(System.currentTimeMillis(), "Dollar", "Pound", 100.00)
        localDataSource.saveTransactions(conversionTransaction)
        viewModel.getHistory()
        val result = viewModel.transactions.getOrAwaitValue().find { it.timestamp == conversionTransaction.timestamp }
        assertTrue(result != null)
    }

    @Test
    fun testCalculateArea_returnsGood(){
        val result = viewModel.calculateRate(3.0, 3.0)
        assertTrue(result == 9.0)
    }

}