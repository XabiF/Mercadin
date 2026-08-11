package com.xabif.mercadin.apis.corteingles

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class CorteInglesInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
            .newBuilder()
            .header("accept", "application/json")
            .header("response_type", "json")
            .header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:153.0) Gecko/20100101 Firefox/153.0")
            .build()
        return chain.proceed(request)
    }
}