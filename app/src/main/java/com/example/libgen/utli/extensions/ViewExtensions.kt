package com.example.libgen.utli.extensions

import android.view.View

fun View.visible() {
    if (!isVisible()) visibility = View.VISIBLE
}

fun View.gone() {
    if (!isGone()) visibility = View.GONE
}

fun View.isVisible(): Boolean {
    return visibility == View.VISIBLE
}

fun View.isGone(): Boolean {
    return visibility == View.GONE
}