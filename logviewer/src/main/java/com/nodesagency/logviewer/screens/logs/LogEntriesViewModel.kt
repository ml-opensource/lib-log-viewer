package com.nodesagency.logviewer.screens.logs

import androidx.arch.core.util.Function
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.nodesagency.logviewer.data.LogRepository
import com.nodesagency.logviewer.data.model.LogEntry
import com.nodesagency.logviewer.domain.Filter

private const val PAGE_SIZE = 20

internal class LogEntriesViewModel(val categoryId: Long, logRepository: LogRepository) : ViewModel() {

    private val defaultFilter = Filter.Disabled(categoryId)
    val filterLiveData: MutableLiveData<Filter> = MutableLiveData()


    val logEntryList: LiveData<PagedList<LogEntry>> = Transformations.switchMap(filterLiveData, Function { input ->
        return@Function LivePagedListBuilder(logRepository.getLogsFilteredBy(input), PAGE_SIZE).build()
    })

    init {
        filterLiveData.postValue(defaultFilter)
    }


    fun filterByTag() {
        val oldFilter = filterLiveData.value ?: defaultFilter
        filterLiveData.postValue(Filter.ByTag(oldFilter.category, oldFilter.query))
    }

    fun filterByMessage() {
        val oldFilter = filterLiveData.value ?: defaultFilter
        filterLiveData.postValue(Filter.ByMessage(oldFilter.category, oldFilter.query))
    }

    fun filterBySeverity() {
        val oldFilter = filterLiveData.value ?: defaultFilter
        filterLiveData.postValue(Filter.BySeverity(oldFilter.category, oldFilter.query))
    }

    fun changeFilterQuery(query: String) {
        val oldFilter = filterLiveData.value ?: defaultFilter
        oldFilter.query = query
        filterLiveData.postValue(oldFilter)
    }

}