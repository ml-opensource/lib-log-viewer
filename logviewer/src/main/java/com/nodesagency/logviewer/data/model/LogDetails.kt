package com.nodesagency.logviewer.data.model

/**
 * Log entry merged with the category and severity names meant for display
 */
data class LogDetails(
    val timestampMilliseconds: Long,
    val tag: String?,
    val category: String,
    val severity: String,
    val message: String,
    val stackTrace: String?
)