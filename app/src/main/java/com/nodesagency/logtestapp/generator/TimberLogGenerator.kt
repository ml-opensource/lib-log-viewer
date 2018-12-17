package com.nodesagency.logtestapp.generator

import com.nodesagency.logviewer.CommonSeverityLevels
import com.nodesagency.logviewer.Logger
import timber.log.Timber
import kotlin.random.Random

internal class TimberLogGenerator(
    logCount: Int,
    delayBetweenLogsMilliseconds: Long = 0L,
    undelayedInitialLogCount: Int = 0
) : LogGenerator(logCount, delayBetweenLogsMilliseconds, undelayedInitialLogCount) {

    override fun log(messageIndex: Int) {
        val severity = Random.nextInt(CommonSeverityLevels.values().size)
            .let { CommonSeverityLevels.values()[it].severity }

        Logger.category = randomCategory
        Timber.log(severity.id?.toInt() ?: 0, throwable, "${severity.level} message at index $messageIndex")
    }

    private val throwable: Throwable?
        get() = if (Random.nextBoolean()) {
            RuntimeException("Test Throwable")
        } else {
            null
        }

    private val randomCategory: String
        get() = listOf(
            "Calculations",
            "General",
            "Local I/O",
            "Network calls",
            "User profile"
        ).random()
}
