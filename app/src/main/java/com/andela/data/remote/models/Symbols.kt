package com.andela.data.remote.models

import com.google.gson.annotations.SerializedName

data class Symbols(
    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("symbols") var symbols: Map<String, String>? = mutableMapOf()
)