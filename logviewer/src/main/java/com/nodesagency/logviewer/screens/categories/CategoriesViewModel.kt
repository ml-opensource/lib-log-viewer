package com.nodesagency.logviewer.screens.categories

import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import com.nodesagency.logviewer.data.LogRepository

private const val PAGE_SIZE = 20

internal class CategoriesViewModel(logRepository: LogRepository) : ViewModel() {

    private val categoriesDataSource = logRepository.getAlphabeticallySortedCategories()

    val categoryList = LivePagedListBuilder(categoriesDataSource, PAGE_SIZE).build()

}