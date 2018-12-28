package com.nodesagency.logviewer

/**
 * Stores a new log entry with the debug severity level. The function is executed asynchronously, so if you'd like to
 * view the logs after the logging operation is done, you can use the [kotlinx.coroutines.Job] object to wait for the
 * execution to finish.
 *
 * @param message the message to log
 * @param tag the regular log tag
 * @param categoryName the name of the category this log is grouped under, or [GENERAL_CATEGORY_NAME] if not provided
 * @param throwable the optional throwable object to display the stack-trace from
 *
 * @return the job started to store the log
 */
fun Logger.d(
    message: String,
    tag: String? = null,
    categoryName: String = GENERAL_CATEGORY_NAME,
    throwable: Throwable? = null,
    includeScreenshot: Boolean = false
) = Logger.log(
    message = message,
    severityLevel = CommonSeverityLevels.DEBUG.severity.level,
    tag = tag,
    categoryName = categoryName,
    throwable = throwable,
    includeScreenshot = includeScreenshot
)

/**
 * Stores a new log entry with the error severity level. The function is executed asynchronously, so if you'd like to
 * view the logs after the logging operation is done, you can use the [kotlinx.coroutines.Job] object to wait for the
 * execution to finish.
 *
 * @param message the message to log
 * @param tag the regular log tag
 * @param categoryName the name of the category this log is grouped under, or [GENERAL_CATEGORY_NAME] if not provided
 * @param throwable the optional throwable object to display the stack-trace from
 *
 * @return the job started to store the log
 */
fun Logger.e(
    message: String,
    tag: String? = null,
    categoryName: String = GENERAL_CATEGORY_NAME,
    throwable: Throwable? = null,
    includeScreenshot: Boolean = false
) = Logger.log(
    message = message,
    severityLevel = CommonSeverityLevels.ERROR.severity.level,
    tag = tag,
    categoryName = categoryName,
    throwable = throwable,
    includeScreenshot = includeScreenshot
)

/**
 * Stores a new log entry with the info severity level. The function is executed asynchronously, so if you'd like to
 * view the logs after the logging operation is done, you can use the [kotlinx.coroutines.Job] object to wait for the
 * execution to finish.
 *
 * @param message the message to log
 * @param tag the regular log tag
 * @param categoryName the name of the category this log is grouped under, or [GENERAL_CATEGORY_NAME] if not provided
 * @param throwable the optional throwable object to display the stack-trace from
 *
 * @return the job started to store the log
 */
fun Logger.i(
    message: String,
    tag: String? = null,
    categoryName: String = GENERAL_CATEGORY_NAME,
    throwable: Throwable? = null,
    includeScreenshot: Boolean = false
) = Logger.log(
    message = message,
    severityLevel = CommonSeverityLevels.INFO.severity.level,
    tag = tag,
    categoryName = categoryName,
    throwable = throwable,
    includeScreenshot = includeScreenshot
)

/**
 * Stores a new log entry with the verbose severity level. The function is executed asynchronously, so if you'd like to
 * view the logs after the logging operation is done, you can use the [kotlinx.coroutines.Job] object to wait for the
 * execution to finish.
 *
 * @param message the message to log
 * @param tag the regular log tag
 * @param categoryName the name of the category this log is grouped under, or [GENERAL_CATEGORY_NAME] if not provided
 * @param throwable the optional throwable object to display the stack-trace from
 *
 * @return the job started to store the log
 */
fun Logger.v(
    message: String,
    tag: String? = null,
    categoryName: String = GENERAL_CATEGORY_NAME,
    throwable: Throwable? = null,
    includeScreenshot: Boolean = false

) = Logger.log(
    message = message,
    severityLevel = CommonSeverityLevels.VERBOSE.severity.level,
    tag = tag,
    categoryName = categoryName,
    throwable = throwable,
    includeScreenshot = includeScreenshot
)

/**
 * Stores a new log entry with the warning severity level. The function is executed asynchronously, so if you'd like to
 * view the logs after the logging operation is done, you can use the [kotlinx.coroutines.Job] object to wait for the
 * execution to finish.
 *
 * @param message the message to log
 * @param tag the regular log tag
 * @param categoryName the name of the category this log is grouped under, or [GENERAL_CATEGORY_NAME] if not provided
 * @param throwable the optional throwable object to display the stack-trace from
 *
 * @return the job started to store the log
 */
fun Logger.w(
    message: String,
    tag: String? = null,
    categoryName: String = GENERAL_CATEGORY_NAME,
    throwable: Throwable? = null,
    includeScreenshot: Boolean = false

) = Logger.log(
    message = message,
    severityLevel = CommonSeverityLevels.WARNING.severity.level,
    tag = tag,
    categoryName = categoryName,
    throwable = throwable,
    includeScreenshot = includeScreenshot
)

/**
 * Stores a new log entry with the WTF severity level. The function is executed asynchronously, so if you'd like to
 * view the logs after the logging operation is done, you can use the [kotlinx.coroutines.Job] object to wait for the
 * execution to finish.
 *
 * @param message the message to log
 * @param tag the regular log tag
 * @param categoryName the name of the category this log is grouped under, or [GENERAL_CATEGORY_NAME] if not provided
 * @param throwable the optional throwable object to display the stack-trace from
 *
 * @return the job started to store the log
 */
fun Logger.wtf(
    message: String,
    tag: String? = null,
    categoryName: String = GENERAL_CATEGORY_NAME,
    throwable: Throwable? = null,
    includeScreenshot: Boolean = false
) = Logger.log(
    message = message,
    severityLevel = CommonSeverityLevels.WTF.severity.level,
    tag = tag,
    categoryName = categoryName,
    throwable = throwable,
    includeScreenshot = includeScreenshot

)