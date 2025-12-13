package com.xabif.mercadin.apis.mercadona

data class ProductPriceInstructions(
    val unit_price: String,
    val unit_size: String,
    val reference_format: String,
    val reference_price: String,
    val previous_unit_price: String?
)