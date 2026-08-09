package com.xabif.mercadin.apis.bm

import com.xabif.mercadin.src.ProductInfo
import com.xabif.mercadin.src.SourceInstance
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Bm() : SourceInstance {
    private val retrofit : Retrofit = Retrofit.Builder()
        .baseUrl(BmApi.Url)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    private val base_api: BmApi = this.retrofit.create(BmApi::class.java);

    override suspend fun queryProducts(query: String): List<ProductInfo> {
        val res = this.base_api.queryProducts(1, 100, 0, 7, false, query);
        if(res.isSuccessful) {
            return res.body()!!.products.map { product: Product -> product.toProductInfo() };
        }
        else {
            throw RuntimeException("BM API queryProducts failed with code=${res.code()}");
        }
    }

    override suspend fun queryProductById(id: String): ProductInfo {
        val res = this.base_api.queryProduct(id.toInt());
        if(res.isSuccessful) {
            return res.body()!!.toProductInfo();
        }
        else {
            throw RuntimeException("BM API queryProductById failed with code=${res.code()}");
        }
    }
}
