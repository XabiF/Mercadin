package com.xabif.mercadin.apis.bm

data class ProductPriceData(
    val prices: List<ProductPrice>,
    val unitPriceUnitType: String,
    val priceUnitType: String
)
