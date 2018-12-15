package com.nodesagency.logviewer.screens.logs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import com.nodesagency.logviewer.data.LogRepository

private const val PAGE_SIZE = 20
private val DEFAULT_FILTER = LogEntriesViewModel.FilterState.ByTag("")

internal class LogEntriesViewModel(categoryId: Long, logRepository: LogRepository) : ViewModel() {

    private val logEntriesDataSource = logRepository.getChronologicallySortedLogEntries(categoryId)

    val logEntryList = LivePagedListBuilder(logEntriesDataSource, PAGE_SIZE).build()


    val filterLiveData: MutableLiveData<FilterState> = MutableLiveData()


    fun setQuery(query: String) {
        val state = filterLiveData.value ?: DEFAULT_FILTER
        state.query = query
        filterLiveData.postValue(state)
        l
    }


    sealed class FilterState(var query: String) {
        class Disabled(query: String) : FilterState(query)
        class ByMessage(query: String) : FilterState(query)
        class ByTag(query: String) : FilterState(query)
        class BySeverity(query: String) : FilterState(query)


    }

}