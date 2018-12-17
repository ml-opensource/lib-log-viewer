package com.nodesagency.logviewer.screens.logdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nodesagency.logviewer.data.LogRepository
import com.nodesagency.logviewer.data.model.LogDetails
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

internal class  LogDetailsViewModel(private val logRepository: LogRepository, logEntryId: Long) : ViewModel() {

    val logEntryLiveData : MutableLiveData<LogDetails> = MutableLiveData()


    init {
        GlobalScope.launch {
            logEntryLiveData.postValue(logRepository.getLogDetails(logEntryId))
        }
    }

}