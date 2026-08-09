package com.xabif.mercadin.src

import kotlin.collections.List

interface SourceInstance {
    suspend fun queryProducts(query: String) : List<ProductInfo>;
    suspend fun queryProductById(id: String) : ProductInfo;
}
