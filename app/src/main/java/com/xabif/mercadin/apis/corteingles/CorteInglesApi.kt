package com.xabif.mercadin.apis.corteingles

import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CorteInglesApi {
    companion object {
        const val Url = "https://www.elcorteingles.es/supermercado/"
    }

    @GET("buscar/")
    suspend fun queryProducts(
        @Query("question") question: String,
        @Query("catalog") catalog: String,
        @Query("stype") stype: String
    ): Response<QueryResult>
}
