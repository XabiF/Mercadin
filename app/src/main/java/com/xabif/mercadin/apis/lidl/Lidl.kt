package com.xabif.mercadin.apis.lidl

import com.xabif.mercadin.src.ProductInfo
import com.xabif.mercadin.src.SourceInstance
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Lidl() : SourceInstance {
    private val retrofit : Retrofit = Retrofit.Builder()
        .baseUrl(LidlApi.Url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val base_api: LidlApi = this.retrofit.create(LidlApi::class.java)

    override suspend fun queryProducts(query: String): List<ProductInfo> {
        val res = this.base_api.queryProducts("ES", "es_ES", "v2.0.0", query)
        if(res.isSuccessful) {
            return res.body()!!.items.map { item -> item.gridbox.data.toProductInfo() }
        }
        else {
            throw RuntimeException("LIDL API queryProducts failed with code=${res.code()}: ${res.errorBody()!!.string()}")
        }
    }

    override suspend fun queryProductById(id: String): ProductInfo {
        val res = this.base_api.queryProduct(id)
        if(res.isSuccessful) {
            return res.body()!!.toProductInfo()
        }
        else {
            throw RuntimeException("LIDL API queryProductById for id=$id failed with code=${res.code()}: ${res.errorBody()!!.string()}")
        }
    }
}
