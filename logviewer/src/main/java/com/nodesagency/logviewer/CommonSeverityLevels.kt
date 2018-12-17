package com.nodesagency.logviewer

import android.util.Log
import com.nodesagency.logviewer.data.model.Severity

/**
 * Log severity levels almost matching the Android implementation
 */
enum class CommonSeverityLevels(val severity: Severity) {
    VERBOSE(Severity(id = Log.VERBOSE.toLong(), level = "Verbose")),
    DEBUG(Severity(id = Log.DEBUG.toLong(), level = "Debug")),
    INFO(Severity(id = Log.INFO.toLong(), level = "Info")),
    WARNING(Severity(id = Log.WARN.toLong(), level = "Warning")),
    ERROR(Severity(id = Log.ERROR.toLong(), level = "Error")),
    ASSERT(Severity(id = Log.ASSERT.toLong(), level = "Assert")),
    WTF(Severity(id = (Log.ASSERT.toLong() + 1), level = "WTF")),
}
