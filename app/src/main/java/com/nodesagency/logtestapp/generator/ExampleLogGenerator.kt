package com.nodesagency.logtestapp.generator

import com.nodesagency.logviewer.CommonSeverityLevels
import com.nodesagency.logviewer.Logger
import com.nodesagency.logviewer.data.model.Severity
import kotlin.random.Random

internal class ExampleLogGenerator(
    logCount: Int,
    delayBetweenLogsMilliseconds: Long = 0L,
    undelayedInitialLogCount: Int = 0
) : LogGenerator(logCount, delayBetweenLogsMilliseconds, undelayedInitialLogCount) {

    override fun log(messageIndex: Int) {
        val severityLevel = getRandomSeverityLevel()

        Logger.log(
            severityLevel = severityLevel,
            message = generateMessage(messageIndex, severityLevel),
            tag = generateTag(),
            categoryName = generateCategoryName(),
            throwable = createThrowableIfSevere(severityLevel)
        )

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
