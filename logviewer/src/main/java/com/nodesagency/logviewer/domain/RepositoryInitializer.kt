package com.nodesagency.logviewer.domain

import com.nodesagency.logviewer.CommonSeverityLevels
import com.nodesagency.logviewer.concurrency.CoroutineScopeProvider
import com.nodesagency.logviewer.data.LogRepository
import kotlinx.coroutines.launch

internal class RepositoryInitializer(private val logRepository: LogRepository) {

    fun initialize() = prefillSeverities()

    private fun prefillSeverities() = CoroutineScopeProvider.ioScope.launch {
        CommonSeverityLevels
            .values()
            .map(CommonSeverityLevels::severity)
            .forEach { severity -> logRepository.put(severity) }
    }
}