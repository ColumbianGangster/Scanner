package com.spike.scanner.ui

import androidx.annotation.DrawableRes
import androidx.appcompat.app.ActionBar

fun ActionBar.applyToolbarUp(showTitle: Boolean = false, @DrawableRes upIndicatorIcon: Int? = null) {
    apply {
        setDisplayHomeAsUpEnabled(true)
        setDisplayShowHomeEnabled(true)
        setDisplayShowTitleEnabled(showTitle)
        if (upIndicatorIcon != null) setHomeAsUpIndicator(upIndicatorIcon)
    }
}
