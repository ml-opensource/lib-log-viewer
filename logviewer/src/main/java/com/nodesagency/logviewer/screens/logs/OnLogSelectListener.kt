package com.nodesagency.logviewer.screens.logs

interface OnLogSelectListener {
    fun onLogSelected(logEntryId: kotlin.Long, severityId: Long)
}