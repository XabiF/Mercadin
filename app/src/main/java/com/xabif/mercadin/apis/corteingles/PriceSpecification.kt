package com.xabif.mercadin.apis.corteingles

data class PriceSpecification(
    val price: String,
    val salePrice: String?,
    val measurementUnitPrice: String,
    val pum_description: String,
    val has_discount: Boolean?
)
