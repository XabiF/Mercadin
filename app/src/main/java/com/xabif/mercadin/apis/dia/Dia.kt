package com.xabif.mercadin.apis.dia

import android.content.Context
import com.xabif.mercadin.src.ProductInfo
import com.xabif.mercadin.src.Source
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Dia() : Source {
    private val retrofit : Retrofit = Retrofit.Builder()
        .baseUrl(DiaApi.Url)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    private val base_api: DiaApi = this.retrofit.create(DiaApi::class.java);

    override suspend fun queryProducts(query: String): List<ProductInfo> {
        val res = this.base_api.searchItems(query, 1);
        if(res.isSuccessful) {
            return res.body()!!.search_items.map { search: SearchItem -> search.toItem().toProductInfo() };
        }
        else {
            throw RuntimeException("Dia API queryProducts failed with code=${res.code()}");
        }
    }

    override suspend fun queryProductById(id: String): ProductInfo {
        val res = this.base_api.queryItem(id.toInt());
        if(res.isSuccessful) {
            return res.body()!!.product.toItem().toProductInfo();
        }
        else {
            throw RuntimeException("Dia API queryProductById failed with code=${res.code()}");
        }
    }
}
