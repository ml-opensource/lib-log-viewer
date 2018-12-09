package com.nodesagency.logviewer.screens.logs

import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import com.nodesagency.logviewer.data.LogRepository

private const val PAGE_SIZE = 20

internal class LogEntriesViewModel(categoryId: Long, logRepository: LogRepository) : ViewModel() {

    private val logEntriesDataSource = logRepository.getChronologicallySortedLogEntries(categoryId)

    val logEntryList = LivePagedListBuilder(logEntriesDataSource, PAGE_SIZE).build()

}