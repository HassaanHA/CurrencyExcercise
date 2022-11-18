package com.andela.presentation.viewModels


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andela.data.repository.CurrencyRepository
import com.andela.domain.entity.Conversion
import com.andela.domain.entity.Currency
import com.andela.utils.MutableEvent
import com.andela.utils.StateHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository,
) : ViewModel() {

    var _currencyStateFlow : MutableStateFlow<StateHandler<List<Currency>?>> = MutableStateFlow(StateHandler.Empty)

    var currencyStateFlow: StateFlow<StateHandler<List<Currency>?>> = _currencyStateFlow

    val _currencyConvertedValue: MutableStateFlow<StateHandler<Conversion?>> = MutableStateFlow(StateHandler.Empty)

    var from: MutableLiveData<String> = MutableLiveData("from")

    var toText: MutableLiveData<String> = MutableLiveData("to")

    var baseField: MutableLiveData<String> = MutableLiveData("1")

    var convertedField: MutableLiveData<String> = MutableLiveData("")

    val currencyConvertedValue: StateFlow<StateHandler<Conversion?>> = _currencyConvertedValue

    val transactions: MutableLiveData<List<Conversion>> = MutableLiveData()

    private val currentBase: MutableLiveData<String> = MutableLiveData<String>("")

    fun getAllCurrencies() = viewModelScope.launch {
        _currencyStateFlow.value = StateHandler.Loading
        currencyRepository.getAllCurrencies().catch { e ->
            _currencyStateFlow.value = StateHandler.Empty
        }.collect {
            _currencyStateFlow.value = StateHandler.Success(it)
        }
    }

    fun getHistory() = viewModelScope.launch(Dispatchers.IO) {
        val list = currencyRepository.getHistory()
        transactions.postValue(list)
    }

    var _topCurrencyStateFlow : MutableStateFlow<StateHandler<List<Conversion>?>> = MutableStateFlow(StateHandler.Empty)

    var topCurrencyStateFlow: StateFlow<StateHandler<List<Conversion>?>> = _topCurrencyStateFlow

    fun getTopCurrencies() = viewModelScope.launch {

        _topCurrencyStateFlow.value = StateHandler.Empty
        val popularCurrencies = "USD,EUR,JPY,GBP,AUD,CAD,CHF,CNH,SEK,NZD"
        currencyRepository.getTopCurrency(currentBase.value!!, popularCurrencies).catch { e ->
            _topCurrencyStateFlow.value = StateHandler.Failure(e)
        }.collect {
            when(it) {
                is StateHandler.Success -> {
                    _topCurrencyStateFlow.value = StateHandler.Success(it.data)
                }
                is StateHandler.Loading -> {
                    _topCurrencyStateFlow.value = StateHandler.Loading
                }
                is StateHandler.Failure -> {
                    _topCurrencyStateFlow.value = StateHandler.Failure(it.msg)
                }
                else -> {
                    _topCurrencyStateFlow.value = StateHandler.Empty
                }
            }
        }
    }

    private fun getConversion(base: String, conversions: String) = viewModelScope.launch {
        _currencyConvertedValue.value = StateHandler.Loading
        currencyRepository.getConvertedCurrency(base, conversions).catch { e ->
            _currencyConvertedValue.value = StateHandler.Failure(e)
        }.collect {
            when(it) {
                is StateHandler.Success -> {
                    val base = baseField.value?.toDouble() ?: 1.0
                    currentBase.postValue(it.data.baseCurrency)
                    convertedField.postValue("${calculateRate(base, it.data.rate)}")
                    _currencyConvertedValue.value = it
                }
                is StateHandler.Loading -> {
                    _currencyConvertedValue.value = StateHandler.Loading
                }
                is StateHandler.Failure -> {
                    _currencyConvertedValue.value = StateHandler.Failure(it.msg)
                }
                else -> {
                    _currencyConvertedValue.value = StateHandler.Empty
                }
            }
        }
    }

    fun convertDetails() {
        if(!from.value.isNullOrBlank()
            && from.value != "from"
            && !toText.value.isNullOrBlank()
            && toText.value != "to"
        ) {
            getConversion(from.value!!, toText.value!!)
        }
    }

    val loadDetails = MutableEvent<Boolean>(false)
    fun getDetails() {
        if(!currentBase.value.isNullOrBlank()) {
            loadDetails.postObserve(true)
        }
    }

    fun calculateRate(base: Double, convertRate: Double): Double {
        return base * convertRate
    }
}