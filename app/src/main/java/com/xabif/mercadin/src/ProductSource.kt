package com.xabif.mercadin.src

import com.xabif.mercadin.R
import com.xabif.mercadin.apis.action.Action
import com.xabif.mercadin.apis.bm.Bm
import com.xabif.mercadin.apis.mercadona.Mercadona
import com.xabif.mercadin.apis.dia.Dia
import com.xabif.mercadin.apis.carrefour.Carrefour
import com.xabif.mercadin.apis.aldi.Aldi
import com.xabif.mercadin.apis.corteingles.CorteIngles
import com.xabif.mercadin.apis.lidl.Lidl

enum class ProductSource {
    Bm,
    Mercadona,
    Dia,
    Carrefour,
    Aldi,
    CorteIngles,
    Lidl,
    Action;

    fun create() : SourceInstance {
        return when(this) {
            Bm -> Bm()
            Mercadona -> Mercadona()
            Dia -> Dia()
            Carrefour -> Carrefour()
            Aldi -> Aldi()
            CorteIngles -> CorteIngles()
            Lidl -> Lidl()
            Action -> Action()
        }
    }

    fun getNameResource() : Int {
        return when(this) {
            Bm -> R.string.super_bm
            Mercadona -> R.string.super_mercadona
            Dia -> R.string.super_dia
            Carrefour -> R.string.super_carrefour
            Aldi -> R.string.super_aldi
            CorteIngles -> R.string.super_corteingles
            Lidl -> R.string.super_lidl
            Action -> R.string.super_action
        }
    }

    fun getColorResource() : Int {
        return when(this) {
            Bm -> R.color.color_bm
            Mercadona -> R.color.color_mercadona
            Dia -> R.color.color_dia
            Carrefour -> R.color.color_carrefour
            Aldi -> R.color.color_aldi
            CorteIngles -> R.color.color_corteingles
            Lidl -> R.color.color_lidl
            Action -> R.color.color_action
        }
    }
}
