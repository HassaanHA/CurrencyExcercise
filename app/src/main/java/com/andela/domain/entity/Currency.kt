package com.andela.domain.entity

data class Currency(
    val title: String,
    val country: String?
) {
    override fun toString(): String {
        return title
    }
}
