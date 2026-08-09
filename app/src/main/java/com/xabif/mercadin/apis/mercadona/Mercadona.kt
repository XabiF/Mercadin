package com.xabif.mercadin.apis.mercadona

import com.xabif.mercadin.src.ProductInfo
import com.xabif.mercadin.src.SourceInstance
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Mercadona() : SourceInstance {
    private val base_retrofit : Retrofit = Retrofit.Builder()
        .baseUrl(MercadonaApi.Url)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    private val base_api: MercadonaApi = this.base_retrofit.create(MercadonaApi::class.java);

    private val algolia_retrofit : Retrofit = Retrofit.Builder()
        .baseUrl(MercadonaAlgoliaApi.Url)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    private val algolia_api: MercadonaAlgoliaApi = this.algolia_retrofit.create(MercadonaAlgoliaApi::class.java);

    override suspend fun queryProducts(query: String): List<ProductInfo> {
        val query_req = QueryRequest(query, true, listOf("web"), true, true);
        val res = algolia_api.queryProducts(MercadonaApi.SampleLang, MercadonaApi.SampleWarehouse, MercadonaAlgoliaApi.Agent, MercadonaAlgoliaApi.ApplicationId, MercadonaAlgoliaApi.ApiKey, query_req);
        if(res.isSuccessful) {
            return res.body()!!.hits.map { product: Product -> product.toProductInfo() };
        }
        else {
            throw RuntimeException("Mercadona API queryProducts failed with code=${res.code()}");
        }
    }

    override suspend fun queryProductById(id: String): ProductInfo {
        val res = base_api.queryProduct(id.toInt(), MercadonaApi.SampleLang, MercadonaApi.SampleWarehouse);
        if(res.isSuccessful) {
            return res.body()!!.toProductInfo();
        }
        else {
            throw RuntimeException("Mercadona API queryProductById failed with code=${res.code()}");
        }
    }
}