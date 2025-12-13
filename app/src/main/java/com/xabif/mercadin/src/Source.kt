package com.xabif.mercadin.src

import android.content.Context
import kotlin.collections.List

interface Source {
    suspend fun queryProducts(query: String) : List<ProductInfo>;
    suspend fun queryProductById(id: String) : ProductInfo;
}
