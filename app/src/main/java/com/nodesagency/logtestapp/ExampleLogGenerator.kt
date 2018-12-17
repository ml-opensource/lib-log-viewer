package com.nodesagency.logtestapp

import com.nodesagency.logviewer.CommonSeverityLevels
import com.nodesagency.logviewer.Logger
import kotlinx.coroutines.*
import timber.log.Timber
import kotlin.random.Random

internal class ExampleLogGenerator(
    private val logCount: Int,
    private val delayBetweenLogsMilliseconds: Long = 0L,
    private val undelayedInitialLogCount: Int = 0,
    private val useTimber: Boolean = false
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

                val priorityLevel = getRandomPriorityLevel()
                val severityLevel = getSevirityLevelByPriority(priorityLevel)

                if (useTimber) {
                    Logger.tree.category = generateCategoryName()
                    Timber.tag(generateTag())
                    Timber.log(
                        priorityLevel,
                        createThrowableIfSevere(severityLevel),
                        generateMessage(logIndex, severityLevel)
                    )
                } else {
                    Logger.log(
                        severityLevel = severityLevel,
                        message = generateMessage(logIndex, severityLevel),
                        tag = generateTag(),
                        categoryName = generateCategoryName(),
                        throwable = createThrowableIfSevere(severityLevel)
                    )
                }

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
            .filter(
                arrayOf(
                    CommonSeverityLevels.ERROR.severity.level,
                    CommonSeverityLevels.ASSERT.severity.level,
                    CommonSeverityLevels.WTF.severity.level
                )::contains
            )

        return if (errorSeverities.contains(severityLevel)) {
            RuntimeException("This is an example exception")
        } else {
            null
        }
    }

    private fun getRandomPriorityLevel(): Int {
        return Random.nextInt(CommonSeverityLevels.values().size).let {
            CommonSeverityLevels.values()[it].severity.id?.toInt() ?: 0
        }
    }

    private fun getSevirityLevelByPriority(priority: Int): String {
        return CommonSeverityLevels.values().first { it.severity.id?.toInt() ?: 0 == priority }.severity.level
    }
}
