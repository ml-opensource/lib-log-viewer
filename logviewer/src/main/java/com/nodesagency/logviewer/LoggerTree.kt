package com.nodesagency.logviewer

import timber.log.Timber


class LoggerTree : Timber.DebugTree() {

    var category: String = GENERAL_CATEGORY_NAME

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        Logger.log(
            severityLevel = CommonSeverityLevels.values().first { it.severity.id?.toInt() ?: 0 == priority }.severity.level,
            message = message,
            throwable = t,
            categoryName = category,
            tag = tag
        )
    }
}
