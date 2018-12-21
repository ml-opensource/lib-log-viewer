package com.nodesagency.logviewer.utils

internal fun Boolean.toInt() : Int {
    return if (this) 1 else 0
}

internal fun Int.toBoolean() : Boolean = this == 1