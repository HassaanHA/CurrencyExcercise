package com.andela.data.dto

import com.andela.domain.entity.Currency

fun Map<String, String>.convertToCurrencyModel(): List<Currency> {
    return this.map {
        Currency(it.key, it.value)
    }
}