package com.nodesagency.logviewer.screens.logdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nodesagency.logviewer.data.LogRepository

internal class LogDetailsViewModelFactory(
    private val logRepository: LogRepository,
    private val logEntryId: Long
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LogDetailsViewModel(logRepository, logEntryId) as T
    }

}