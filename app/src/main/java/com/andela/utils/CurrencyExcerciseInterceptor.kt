package com.andela.utils

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class CurrencyExcerciseInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .apply {
                addHeader("apikey", NetworkUtil.apiKey)
            }
            .build()
        return chain.proceed(request)
    }
}