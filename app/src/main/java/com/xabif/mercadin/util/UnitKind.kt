package com.xabif.mercadin.util

enum class UnitKind {
    SingleUnit,
    Liter,
    Milliliter,
    Centiliter,
    Gram,
    Kilogram,
    Meter,
    Dozen;

    fun format() : String {
        if(this == SingleUnit) {
            return "ud.";
        }
        else if(this == Liter) {
            return "L";
        }
        else if(this == Milliliter) {
            return "mL";
        }
        else if(this == Centiliter) {
            return "cL";
        }
        else if(this == Gram) {
            return "g";
        }
        else if(this == Kilogram) {
            return "kg";
        }
        else if(this == Meter) {
            return "m";
        }
        else if(this == Dozen) {
            return "dc";
        }
        else {
            throw RuntimeException("Invalid unit value");
        }
    }
}