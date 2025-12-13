package com.xabif.mercadin.apis.bm

data class QueryResult(
    val totalCount: Int,
    val hasMore: Boolean,
    val products: List<Product>
)
