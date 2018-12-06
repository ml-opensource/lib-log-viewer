package com.nodesagency.logviewer.screens.log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nodesagency.logviewer.data.LogRepository

internal class LogEntriesViewModelFactory(
    private val categoryId: Long,
    private val logRepository: LogRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LogEntriesViewModel(categoryId, logRepository) as T
    }

}