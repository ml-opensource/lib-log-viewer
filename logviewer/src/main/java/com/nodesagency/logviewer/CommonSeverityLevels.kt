package com.nodesagency.logviewer

import com.nodesagency.logviewer.data.model.Severity

/**
 * Log severity levels matching the Android implementation
 */
enum class CommonSeverityLevels(val severity: Severity) {
    VERBOSE(Severity(id = 0, level = "Verbose")),
    DEBUG(Severity(id = 1, level = "Debug")),
    INFO(Severity(id = 2, level = "Info")),
    WARNING(Severity(id = 3, level = "Warning")),
    ERROR(Severity(id = 4, level = "Error")),
    ASSERT(Severity(id = 5, level = "Assert")),
    WTF(Severity(id = 6, level = "WTF")),
}