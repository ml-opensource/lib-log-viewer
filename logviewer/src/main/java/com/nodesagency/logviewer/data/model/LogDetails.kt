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
    val stackTrace: String?,
    val screenshotUri: String?
) {

    fun toShareMessage() : String {
        return "Log details: \n" +
                "Timestamp: $timestampMilliseconds\n" +
                "Category: $category\n" +
                "Severity: $severity\n" +
                "Message: $message\n" +
                "Stack Trace: $stackTrace"
    }

}