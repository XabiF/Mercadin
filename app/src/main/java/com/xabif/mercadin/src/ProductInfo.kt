package com.xabif.mercadin.src

import com.xabif.mercadin.util.Price
import com.xabif.mercadin.util.parseUnit
import com.xabif.mercadin.util.reduce
import org.json.JSONObject

class ProductInfo(val source: ProductSource, val id: String, val name: String, val price: Price, val sale_price: Price?, val image_url: String, val url: String) {
    constructor(source: ProductSource, id: String, name: String, price_val: Float, sale_price_val: Float?, ref_text: String, ref_price: Float, sale_ref_price: Float?, image_url: String, url: String)
            : this(source, id, name, Price(price_val, ref_price, reduce(parseUnit(ref_text))), sale_price_val?.let { Price(it, sale_ref_price!!, reduce(parseUnit(ref_text))) }, image_url, url)

    fun actualUnitPrice() : Float {
        if(sale_price != null) {
            return sale_price.unit_price
        }
        else {
            return price.unit_price
        }
    }

    fun actualProductPrice() : Float {
        if(sale_price != null) {
            return sale_price.product_price
        }
        else {
            return price.product_price
        }
    }

    constructor(json: JSONObject) : this(ProductSource.entries[json.getInt("source")], json.getString("id"), json.getString("name"), Price(json.getJSONObject("price")), if(json.has("sale_price")) Price(json.getJSONObject("sale_price")) else null, json.getString("image_url"), json.getString("url"))

    fun toJson() : JSONObject {
        val product_obj = JSONObject()
        product_obj.put("source", source.ordinal)
        product_obj.put("id", id)
        product_obj.put("name", name)
        product_obj.put("price", price.toJson())
        if(sale_price != null) {
            product_obj.put("sale_price", sale_price.toJson())
        }
        product_obj.put("image_url", image_url)
        return product_obj
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ProductInfo) return false

        return (source == other.source) && (id == other.id)
    }

    override fun hashCode(): Int = listOf(id, source).hashCode()

    fun isSale() = sale_price != null
}
