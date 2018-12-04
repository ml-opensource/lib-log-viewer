package com.nodesagency.logviewer.screens.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.nodesagency.logviewer.data.LogRepository
import com.nodesagency.logviewer.data.model.Category

private const val PAGE_SIZE = 20

class CategoriesViewModel(
    logRepository: LogRepository
) : ViewModel() {

    private val categoriesDataSource = logRepository.getAllCategoriesAlphabeticallySorted()

    val categoryList: LiveData<PagedList<Category>> = LivePagedListBuilder(categoriesDataSource, PAGE_SIZE).build()
}