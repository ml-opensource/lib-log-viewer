package com.nodesagency.logtestapp.generator

import kotlinx.coroutines.*

internal abstract class LogGenerator(
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

                log(logIndex)

                if (logIndex >= undelayedInitialLogCount) {
                    delay(delayBetweenLogsMilliseconds)
                }
            }
        }
    }

    abstract fun log(messageIndex: Int)
}
