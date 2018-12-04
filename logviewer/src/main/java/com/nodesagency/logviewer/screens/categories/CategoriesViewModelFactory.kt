package com.nodesagency.logviewer.screens.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nodesagency.logviewer.data.LogRepository

internal class CategoriesViewModelFactory(
    private val logRepository: LogRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CategoriesViewModel(logRepository) as T
    }

}