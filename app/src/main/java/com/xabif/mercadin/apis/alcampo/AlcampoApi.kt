package com.xabif.mercadin.apis.alcampo

import com.xabif.mercadin.apis.action.QueryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AlcampoApi {
    companion object {
        const val Url = "https://www.compraonline.alcampo.es/api/webproductpagews/"
    }

    @GET("v6/product-pages/search")
    suspend fun search(
        @Query("includeAdditionalPageInfo") includeAdditionalPageInfo: Boolean,
        @Query("maxPageSize") maxPageSize: Int,
        @Query("maxProductsToDecorate") maxProductsToDecorate: Int,
        @Query("q") q: String,
        @Query("q") tag: String,
    ): Response<QueryResponse>
}
