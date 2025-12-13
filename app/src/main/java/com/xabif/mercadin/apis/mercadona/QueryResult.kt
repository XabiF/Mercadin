package com.xabif.mercadin.apis.mercadona

data class QueryResult(
    val hits: List<Product>,
    val nbHits: Int,
    val page: Int,
    val nbPages: Int,
    val hitsPerPage: Int
)
