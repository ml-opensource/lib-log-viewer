package com.nodesagency.logtestapp.generator

import android.annotation.SuppressLint
import com.nodesagency.logviewer.CommonSeverityLevels
import com.nodesagency.logviewer.Logger
import timber.log.Timber
import kotlin.random.Random

internal class TimberLogGenerator(
    logCount: Int,
    delayBetweenLogsMilliseconds: Long = 0L,
    undelayedInitialLogCount: Int = 0
) : LogGenerator(logCount, delayBetweenLogsMilliseconds, undelayedInitialLogCount) {

    @SuppressLint("TimberExceptionLogging")
    private val logV: (Throwable?, String) -> Unit = { t, message -> Timber.v(t, message) }
    @SuppressLint("TimberExceptionLogging")
    private val logD: (Throwable?, String) -> Unit = { t, message -> Timber.d(t, message) }
    @SuppressLint("TimberExceptionLogging")
    private val logI: (Throwable?, String) -> Unit = { t, message -> Timber.i(t, message) }
    @SuppressLint("TimberExceptionLogging")
    private val logW: (Throwable?, String) -> Unit = { t, message -> Timber.w(t, message) }
    @SuppressLint("TimberExceptionLogging")
    private val logE: (Throwable?, String) -> Unit = { t, message -> Timber.e(t, message) }
    @SuppressLint("TimberExceptionLogging")
    private val logWTF: (Throwable?, String) -> Unit = { t, message -> Timber.wtf(t, message) }

    private val logs = listOf(
        logV, logD, logI, logW, logE, logWTF
    )

    private val randomLog: (Throwable?, String) -> Unit
        get() = logs.random()

    override fun log(messageIndex: Int) {
        val severity = Random.nextInt(CommonSeverityLevels.values().size)
            .let { CommonSeverityLevels.values()[it].severity }

        Logger.category = randomCategory
        if (Random.nextInt(10) == 0) {
            Timber.log(severity.id?.toInt() ?: 0, throwable, "$severity: explicit priority message at $messageIndex")
        } else {
            randomLog.invoke(throwable, "$severity: Message at index $messageIndex")
        }
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
