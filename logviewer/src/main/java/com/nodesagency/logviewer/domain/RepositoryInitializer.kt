package com.nodesagency.logviewer.domain

import com.nodesagency.logviewer.CommonSeverityLevels
import com.nodesagency.logviewer.data.LogRepository

internal class RepositoryInitializer(private val logRepository: LogRepository) {
    fun initialize() {
        CommonSeverityLevels
            .values()
            .map(CommonSeverityLevels::severity)
            .forEach { severity -> logRepository.put(severity) }
    }
}