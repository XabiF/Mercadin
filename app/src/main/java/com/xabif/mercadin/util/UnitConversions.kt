package com.xabif.mercadin.util

val UnitMap = mapOf(
    Pair("ud", UnitKind.SingleUnit),
    Pair("l", UnitKind.Liter),
    Pair("g", UnitKind.Gram),
    Pair("cl", UnitKind.Centiliter),
    Pair("ml", UnitKind.Milliliter),
    Pair("kg", UnitKind.Kilogram),

    Pair("docena", UnitKind.Dozen),
    Pair("dc", UnitKind.Dozen),
    Pair("dc.", UnitKind.Dozen),

    Pair("metro", UnitKind.Meter),
    Pair("m", UnitKind.Meter),

    Pair("unidad", UnitKind.SingleUnit),
    Pair("litro", UnitKind.Liter),
    Pair("kilo", UnitKind.Kilogram),
    Pair("kg.", UnitKind.Kilogram),
    Pair("gr", UnitKind.Gram),
    Pair("gr.", UnitKind.Gram),
    Pair("ml.", UnitKind.Milliliter),
    Pair("u.", UnitKind.SingleUnit),

    Pair("lavado", UnitKind.SingleUnit),
    Pair("lv", UnitKind.SingleUnit),
);

/*
* TODO:
*  DC/DOCENA = docena
*  METRO = longitudes (hilo dental, etc)
*  LAVADO/lv (...?)
* */

fun parseUnit(text: String) : Unit {
    if(text.isBlank()) {
        return Unit(UnitKind.SingleUnit, 1.0f);
    }

    val tokens = text.split(" ");
    if(tokens.size == 1) {
        // Solamente la unidad
        val unit_str = tokens.first().lowercase();
        val unit_kind = UnitMap[unit_str] ?: throw UnknownUnitException("Unable to parse single-term unit '${text}'");
        return Unit(unit_kind, 1.0f);
    }
    else if(tokens.size == 2) {
        // Solamente la unidad
        val value_str = tokens[0].toFloat();
        val unit_str = tokens[1].lowercase();
        val unit_kind = UnitMap[unit_str] ?: throw UnknownUnitException("Unable to parse two-term unit '${text}'");
        return Unit(unit_kind, value_str);
    }
    else {
        throw UnknownUnitException("Unable to parse unit '${text}'");
    }
}

fun reduce(unit: Unit) : Unit {
    if(unit.kind == UnitKind.SingleUnit) {
        return unit;
    }
    else if(unit.kind == UnitKind.Gram) {
        val val_kg = 1000.0f / unit.value;
        return Unit(UnitKind.Kilogram, val_kg);
    }
    else if(unit.kind == UnitKind.Kilogram) {
        return unit;
    }
    else if(unit.kind == UnitKind.Liter) {
        return unit;
    }
    else if(unit.kind == UnitKind.Meter) {
        return unit;
    }
    else if(unit.kind == UnitKind.Milliliter) {
        val val_l = 1000.0f / unit.value;
        return Unit(UnitKind.Liter, val_l);
    }
    else if(unit.kind == UnitKind.Centiliter) {
        val val_l = 100.0f / unit.value;
        return Unit(UnitKind.Liter, val_l);
    }
    else if(unit.kind == UnitKind.Dozen) {
        val val_l = unit.value / 12;
        return Unit(UnitKind.SingleUnit, val_l);
    }
    else {
        throw RuntimeException("Invalid unit");
    }
}