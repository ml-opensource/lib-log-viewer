package com.nodesagency.logtestapp

import com.nodesagency.logviewer.CommonSeverityLevels
import com.nodesagency.logviewer.Logger
import com.nodesagency.logviewer.data.model.Severity
import kotlinx.coroutines.*
import kotlin.random.Random

internal class ExampleLogGenerator(
    private val logCount: Int,
    private val delayBetweenLogsMilliseconds: Long = 0L,
    private val undelayedInitialLogCount: Int = 0
) {
    private var scope: CoroutineScope? = null

    fun start() {
        scope = CoroutineScope(kotlinx.coroutines.Dispatchers.Default)
        generateLogs()
    }

    fun stop() {
        scope?.coroutineContext?.cancel()
    }

    private fun generateLogs() = runBlocking {
        scope?.launch {
            for (logIndex in 0 until logCount) {

                if (!isActive) {
                    return@launch
                }

                val severityLevel = getRandomSeverityLevel()

                Logger.log(
                    severityLevel = severityLevel,
                    message = generateMessage(logIndex, severityLevel),
                    tag = generateTag(),
                    categoryName = generateCategoryName(),
                    throwable = createThrowableIfSevere(severityLevel)
                )

                if (logIndex >= undelayedInitialLogCount) {
                    delay(delayBetweenLogsMilliseconds)
                }
            }
        }
    }

    private fun generateMessage(logIndex: Int, severityLevel: String): String {
        return "$severityLevel message at index $logIndex"
    }

    private fun generateTag(): String {
        return arrayOf(
            "SampleTag",
            "LoggedActivity",
            "LoggedFragment",
            "TestTag",
            "YetAnotherTag"
        ).random()
    }

    private fun generateCategoryName(): String {
        return arrayOf(
            "Calculations",
            "General",
            "Local I/O",
            "Network calls",
            "User profile"
        ).random()
    }

    private fun createThrowableIfSevere(severityLevel: String): Throwable? {
        val errorSeverities = CommonSeverityLevels
            .values()
            .map { it.severity.level }
            .filter(arrayOf(
                CommonSeverityLevels.ERROR.severity.level,
                CommonSeverityLevels.ASSERT.severity.level,
                CommonSeverityLevels.WTF.severity.level)::contains)

        return if (errorSeverities.contains(severityLevel)) {
            RuntimeException("This is an example exception")
        } else {
            null
        }
    }

    private fun getRandomSeverityLevel(): String {
        val commonSeverities = CommonSeverityLevels.values().map { it.severity }
        val severityTypeCount = commonSeverities.size

        return Random
            .nextInt(severityTypeCount)
            .let(commonSeverities::get)
            .let(Severity::level)
    }
}
