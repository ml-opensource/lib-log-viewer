package com.nodesagency.logviewer

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.nodesagency.logviewer.concurrency.CoroutineScopeProvider
import com.nodesagency.logviewer.data.LogRepository
import com.nodesagency.logviewer.data.database.DatabaseLogRepository
import com.nodesagency.logviewer.data.model.Category
import com.nodesagency.logviewer.data.model.LogEntry
import com.nodesagency.logviewer.data.model.Severity
import com.nodesagency.logviewer.domain.RepositoryInitializer
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.PrintWriter
import java.io.StringWriter

internal const val GENERAL_CATEGORY_ID = 0L
internal const val GENERAL_CATEGORY_NAME = "General"


object Logger {

    private var isInitialized = false
    private lateinit var logRepository: LogRepository

    fun initialize(
        context: Context,
        logRepository: LogRepository = createDefaultLogRepository(context)
    ) = runBlocking {
        if (isInitialized) {
            throw IllegalStateException(
                "It seems that Logger has already been initialized somewhere else. You cannot call initialize() twice."
            )
        }

        this@Logger.logRepository = logRepository

        val repositoryInitializationJob = RepositoryInitializer(Logger.logRepository).initialize()

        repositoryInitializationJob.join()

        if (!repositoryInitializationJob.isCancelled) {
            isInitialized = true
        }
    }

    /**
     * Stores a new log entry. The function is executed asynchronously, so if you'd like to view the logs after the
     * logging operation is done, you can use the [kotlinx.coroutines.Job] object to wait for the execution to finish.
     *
     * @param message the message to log
     * @param severityLevel the severity of the logged event, e.g. "Info", "Error" etc. Also see [CommonSeverityLevels]
     * @param tag the regular log tag
     * @param categoryName the name of the category this log is grouped under, or [GENERAL_CATEGORY_NAME] if not provided
     * @param throwable the optional throwable object to display the stack-trace from
     *
     * @return the job started to store the log
     */
    fun log(
        message: String,
        severityLevel: String = CommonSeverityLevels.VERBOSE.severity.level,
        tag: String? = null,
        categoryName: String = GENERAL_CATEGORY_NAME,
        throwable: Throwable? = null
    ) = doAfterInitializationCheck {
        runBlocking {
            val job = CoroutineScopeProvider.ioScope.launch {
                storeLog(
                    categoryName = categoryName,
                    severityLevel = severityLevel,
                    message = message,
                    throwable = throwable,
                    tag = tag
                )
            }

            job.join() // Assures the jobs are stored in calling order

            return@runBlocking job
        }
    }

    private fun storeLog(
        categoryName: String,
        severityLevel: String,
        message: String,
        throwable: Throwable?,
        tag: String?
    ) {
        val categoryId = logRepository.getIdForCategoryName(categoryName) ?: insertNewCategory(categoryName)
        val severityId = logRepository.getIdForSeverityLevel(severityLevel) ?: insertNewSeverity(severityLevel)

        val logEntry = LogEntry(
            categoryId = categoryId,
            severityId = severityId,
            message = message,
            stackTrace = throwable?.getStackTraceAsString(),
            tag = tag,
            timestampMilliseconds = System.currentTimeMillis()
        )

        // TODO: Notify features about the new log entry

        logRepository.put(logEntry)
    }

    internal fun getRepository(): LogRepository = doAfterInitializationCheck { logRepository }

    /**
     * WARNING: Do not use, only for testing!
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun deinitialize() {
        isInitialized = false
    }

    private fun <T> doAfterInitializationCheck(doAfter: () -> T): T {
        if (!isInitialized) {
            throw IllegalStateException(
                "You need to call Logger.initialize(...) before using the class. " +
                        "It is recommended to do it during your application's onCreate()."
            )
        }

        return doAfter()
    }

    private fun insertNewCategory(categoryName: String): Long {
        if (categoryName.isBlank()) {
            throw IllegalArgumentException("The name of the category shouldn't be blank")
        }

        return if (categoryName == GENERAL_CATEGORY_NAME) {
            logRepository.put(Category(id = GENERAL_CATEGORY_ID, name = GENERAL_CATEGORY_NAME))
        } else {
            logRepository.put(Category(name = categoryName))
        }
    }

    private fun insertNewSeverity(severityLevel: String): Long {
        if (severityLevel.isBlank()) {
            throw IllegalArgumentException("The severity level shouldn't be blank")
        }

        return logRepository.put(Severity(level = severityLevel))
    }

    private fun createDefaultLogRepository(context: Context): LogRepository = DatabaseLogRepository(context)
}

private fun Throwable.getStackTraceAsString(): String {
    return StringWriter()
        .apply { printStackTrace(PrintWriter(this)) }
        .toString()
}
