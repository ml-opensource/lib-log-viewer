package com.nodesagency.logviewer.screens.logdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nodesagency.logviewer.data.LogRepository
import com.nodesagency.logviewer.data.model.LogDetails
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

internal class  LogDetailsViewModel(private val logRepository: LogRepository, logEntryId: Long) : ViewModel() {

    private val mLogEntryLiveData : MutableLiveData<LogDetails> = MutableLiveData()

    val logEntryLiveData: LiveData<LogDetails>
        get() = mLogEntryLiveData

    init {
        GlobalScope.launch {
            mLogEntryLiveData.postValue(logRepository.getLogDetails(logEntryId))
        }
    }

}