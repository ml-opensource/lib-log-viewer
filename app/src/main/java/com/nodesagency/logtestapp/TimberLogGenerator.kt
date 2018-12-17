package com.nodesagency.logtestapp

import com.nodesagency.logviewer.CommonSeverityLevels
import kotlinx.coroutines.*
import timber.log.Timber
import kotlin.random.Random

internal class TimberLogGenerator(
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

                val severity = Random.nextInt(CommonSeverityLevels.values().size).let { CommonSeverityLevels.values()[it].severity }
                Timber.log(severity.id?.toInt() ?: 0, "Some Message")

                if (logIndex >= undelayedInitialLogCount) {
                    delay(delayBetweenLogsMilliseconds)
                }
            }
        }
    }
}
