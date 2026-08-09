package com.xabif.mercadin.src

import com.xabif.mercadin.R
import com.xabif.mercadin.apis.aldi.Aldi
import com.xabif.mercadin.apis.bm.Bm
import com.xabif.mercadin.apis.carrefour.Carrefour
import com.xabif.mercadin.apis.dia.Dia
import com.xabif.mercadin.apis.mercadona.Mercadona

enum class ProductSource {
    Bm,
    Mercadona,
    Dia,
    Carrefour,
    Aldi;

    fun create() : SourceInstance {
        return when(this) {
            Bm -> Bm()
            Mercadona -> Mercadona()
            Dia -> Dia()
            Carrefour -> Carrefour()
            Aldi -> Aldi()
        }
    }

    fun getNameResource() : Int {
        return when(this) {
            Bm -> R.string.super_bm
            Mercadona -> R.string.super_mercadona
            Dia -> R.string.super_dia
            Carrefour -> R.string.super_carrefour
            Aldi -> R.string.super_aldi
        }
    }

    fun getColorResource() : Int {
        return when(this) {
            Bm -> R.color.colorBm
            Mercadona -> R.color.colorMercadona
            Dia -> R.color.colorDia
            Carrefour -> R.color.colorCarrefour
            Aldi -> R.color.colorAldi
        }
    }
}
