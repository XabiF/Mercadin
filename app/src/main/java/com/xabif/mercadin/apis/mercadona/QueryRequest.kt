package com.xabif.mercadin.apis.mercadona

data class QueryRequest(
    val query: String,
    val clickAnalytics: Boolean,
    val analyticsTags: List<String>,
    val getRankingInfo: Boolean,
    val analytics: Boolean
)
