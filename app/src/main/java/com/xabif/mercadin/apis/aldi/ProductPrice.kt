package com.xabif.mercadin.apis.aldi

data class ProductPrice(
    val priceValue: Float,
    val basePrice: List<ProductBasePrice>?
)
