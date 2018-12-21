package com.nodesagency.logviewer.screens.categories

import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import com.nodesagency.logviewer.data.LogRepository
import com.nodesagency.logviewer.data.model.Category
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private const val PAGE_SIZE = 20

internal class CategoriesViewModel(val logRepository: LogRepository) : ViewModel() {

    private val categoriesDataSource = logRepository.getAlphabeticallySortedCategories()

    val categoryList = LivePagedListBuilder(categoriesDataSource, PAGE_SIZE).build()


    fun pinCategory(category: Category) {
        GlobalScope.launch {
            logRepository.pinCategory(category.id!!, !category.isPinned)
        }
    }

    fun clearAllLogs() {
        GlobalScope.launch {
            logRepository.deleteAllCategoriesAndLogs()
        }
    }

}