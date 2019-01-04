package com.nodesagency.logviewer

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.VisibleForTesting
import com.nodesagency.logviewer.concurrency.CoroutineScopeProvider
import com.nodesagency.logviewer.data.LogRepository
import com.nodesagency.logviewer.data.ScreenshotRepository
import com.nodesagency.logviewer.data.ScreenshotRepositoryImpl
import com.nodesagency.logviewer.data.database.DatabaseLogRepository
import com.nodesagency.logviewer.data.model.Category
import com.nodesagency.logviewer.data.model.LogEntry
import com.nodesagency.logviewer.data.model.Severity
import com.nodesagency.logviewer.domain.RepositoryInitializer
import kotlinx.coroutines.*
import java.io.PrintWriter
import java.io.StringWriter
import android.graphics.drawable.Drawable
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import java.lang.ref.WeakReference


internal const val GENERAL_CATEGORY_ID = 0L
internal const val GENERAL_CATEGORY_NAME = "General"

@SuppressLint("staticfieldleak")
object Logger {

    private var isInitialized = false
    private var currentScreen: WeakReference<Activity?>? = null
    private lateinit var logRepository: LogRepository
    private lateinit var screenshotRepository: ScreenshotRepository

    /**
     * Initializes the library. This has to be called before any logging is performed. Usually you'll want to do this in
     * your application's `onCreate()` method.
     *
     * @param context the context your [Logger] instance is initialized with. You'll probably want to use your
     * application context here.
     * @param logRepository if you want to use your custom log storage instead of the default one, this is where you can
     * provide it
     */
    fun initialize(
        context: Context,
        screenshotRepository: ScreenshotRepository = ScreenshotRepositoryImpl(context),
        logRepository: LogRepository = createDefaultLogRepository(context)
    ): Unit = runBlocking {

        if (isInitialized) {
            throw IllegalStateException(
                "It seems that Logger has already been initialized somewhere else. You cannot call initialize() twice."
            )
        }

        this@Logger.logRepository = logRepository
        this@Logger.screenshotRepository = screenshotRepository

        joinInIoScope {
            RepositoryInitializer(Logger.logRepository).initialize()

            if (it.isActive) {
                isInitialized = true
            }
        }.join()
        context.addScreenChangedListener(
            onScreenClosed = { currentScreen = null },
            onScreenOpened = { currentScreen = WeakReference(it)}
        )

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
     * @param includeScreenshot indicates whether or not screenshot will be included with log, false by default
     *
     * @return the job started to store the log
     */
    fun log(
        message: String,
        severityLevel: String = CommonSeverityLevels.VERBOSE.severity.level,
        tag: String? = null,
        categoryName: String = GENERAL_CATEGORY_NAME,
        throwable: Throwable? = null,
        includeScreenshot: Boolean = false
    ): Job = doAfterInitializationCheck {
        joinInIoScope {
            storeLog(
                categoryName = categoryName,
                severityLevel = severityLevel,
                message = message,
                throwable = throwable,
                tag = tag,
                includeScreenshot = includeScreenshot
            )
        }
    }

    private fun joinInIoScope(runInIoScope: suspend (CoroutineScope) -> Unit): Job {
        return runBlocking {
            val job = CoroutineScopeProvider.ioScope.launch {
                runInIoScope(this)
            }

            job.join() // Assures the logs are stored in calling order

            job
        }
    }

    /**
     * Deletes all logs. The function is executed asynchronously, so if you'd like to view the logs after the
     * logging operation is done, you can use the returned [kotlinx.coroutines.Job] object to wait for the execution to
     * finish.
     *
     * @return the job started to deleteAllCategoriesAndLogs all logs
     */
    fun clearAllLogs(): Job = doAfterInitializationCheck {
        joinInIoScope {
            logRepository.deleteAllCategoriesAndLogs()
            screenshotRepository.deleteAllScreenshots()
        }
    }

    private fun storeLog(
        categoryName: String,
        severityLevel: String,
        message: String,
        throwable: Throwable?,
        tag: String?,
        includeScreenshot: Boolean
    ) {
        val categoryId = logRepository.getIdForCategoryName(categoryName) ?: insertNewCategory(categoryName)
        val severityId = logRepository.getIdForSeverityLevel(severityLevel) ?: insertNewSeverity(severityLevel)

        val screenshotUri = if (includeScreenshot) storeScreenshot() else null
        val logEntry = LogEntry(
            categoryId = categoryId,
            severityId = severityId,
            message = message,
            stackTrace = throwable?.getStackTraceAsString(),
            tag = tag,
            timestampMilliseconds = System.currentTimeMillis(),
            screenshotUri = screenshotUri?.toString()
        )

        // TODO: Notify features about the new log entry

        logRepository.put(logEntry)
    }

    private fun storeScreenshot() : Uri? {
        val screenView = currentScreen?.get()?.window?.findViewById<View>(android.R.id.content)?.rootView ?: return null
        val bitmap = Bitmap.createBitmap(screenView.width, screenView.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        screenView.draw(canvas)
        return screenshotRepository.saveScreenshot(bitmap)
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


private fun Context.addScreenChangedListener(onScreenOpened: (Activity?) -> Unit, onScreenClosed: () -> Unit) {
    (this as Application).registerActivityLifecycleCallbacks( object : Application.ActivityLifecycleCallbacks {


        override fun onActivityPaused(p0: Activity?) {
            onScreenClosed()
        }

        override fun onActivityResumed(p0: Activity?) {
            onScreenOpened(p0)
        }

        override fun onActivityStarted(p0: Activity?) {}
        override fun onActivityDestroyed(p0: Activity?) {}
        override fun onActivitySaveInstanceState(p0: Activity?, p1: Bundle?) {}
        override fun onActivityStopped(p0: Activity?) {}
        override fun onActivityCreated(p0: Activity?, p1: Bundle?) {}
    })

}