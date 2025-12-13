package com.xabif.mercadin.ui

import android.graphics.Paint
import android.graphics.Typeface
import android.widget.TextView

class Util {
    companion object {
        fun crossText(view: TextView) {
            view.paintFlags = view.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG;
        }

        fun uncrossText(view: TextView) {
            view.paintFlags = view.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv();
        }

        fun boldText(view: TextView) {
            view.setTypeface(null, Typeface.BOLD);
        }

        fun italicText(view: TextView) {
            view.setTypeface(null, Typeface.ITALIC);
        }
    }
}