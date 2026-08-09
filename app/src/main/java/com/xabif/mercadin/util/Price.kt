package com.xabif.mercadin.util

import android.annotation.SuppressLint
import org.json.JSONObject
import java.util.Locale

data class Price(
    val product_price: Float,
    val unit_price: Float,
    val kind: UnitKind,
) {
    constructor(product_price: Float, unit_price: Float, unit: Unit) : this(product_price, unit_price / unit.value, unit.kind)

    fun formatProductPrice() : String {
        return "${String.format("%.2f", product_price)} €"
    }

    fun formatUnitPrice() : String {
        return "${String.format("%.2f", unit_price)} € / ${kind.format()}"
    }

    constructor(json: JSONObject) : this(json.getDouble("product_price").toFloat(), json.getDouble("product_price").toFloat(), UnitKind.entries[json.getInt("kind")])

    fun toJson() : JSONObject {
        val price_json = JSONObject()
        price_json.put("product_price", product_price.toDouble())
        price_json.put("unit_price", unit_price.toDouble())
        price_json.put("kind", kind.ordinal)
        return price_json
    }
}
