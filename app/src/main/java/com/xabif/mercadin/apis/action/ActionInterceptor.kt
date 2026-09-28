package com.xabif.mercadin.apis.action

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class ActionInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
            .newBuilder()
            .header("content-type", "application/json")
            .header("Accept", "application/json")
            .header("Accept-Language", "es-ES")
            .header("cache-control", "no-cache")
            .header("x-apollo-operation-name", "SearchProductsWithSuggestions")
            .header("x-client-version", "1.317")
            .header("apollographql-client-name", "web")
            .header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:153.0) Gecko/20100101 Firefox/153.0")
            .build()
        return chain.proceed(request)
    }
}
