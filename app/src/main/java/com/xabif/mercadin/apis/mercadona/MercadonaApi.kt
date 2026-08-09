package com.xabif.mercadin.apis.mercadona

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MercadonaApi {
    companion object {
        const val Url = "https://tienda.mercadona.es/api/"

        const val SampleLang = "es"
        const val SampleWarehouse = 4572
    }

    @GET("products/{id}/")
    suspend fun queryProduct(
        @Path("id") id: Int,
        @Query("lang") lang: String,
        @Query("wh") warehouse: Int
    ): Response<Product>

    /*
    @GET("products/{id}/xselling/")
    suspend fun queryRelatedProducts(
        @Path("id") id: Int,
        @Query("lang") lang: String,
        @Query("wh") warehouse: Int,
        @Query("exclude") exclude: String
    ): Response<RelatedProductPage>
    */
}

