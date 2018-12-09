package com.nodesagency.logviewer.screens.logs

internal interface OnLogSelectListener {
    fun onLogSelected(logEntryId: kotlin.Long, severityId: Long)
}