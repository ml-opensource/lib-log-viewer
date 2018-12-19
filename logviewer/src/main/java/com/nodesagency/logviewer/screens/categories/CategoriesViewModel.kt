package com.nodesagency.logviewer.screens.categories

import android.net.Uri
import android.util.Log
import androidx.arch.core.util.Function
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.nodesagency.logviewer.data.LogRepository
import com.nodesagency.logviewer.data.model.Category
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val PAGE_SIZE = 20

internal class CategoriesViewModel(val logRepository: LogRepository) : ViewModel() {
    val queryLiveData = MutableLiveData<String>()

    val categoryList : LiveData<PagedList<Category>> = Transformations.switchMap(queryLiveData, Function { input ->
        return@Function LivePagedListBuilder(logRepository.getCategoriesByName(input), PAGE_SIZE).build()
    })

    val storageCopyUriLiveData: MutableLiveData<Uri?> = MutableLiveData()

    init {
        queryLiveData.postValue("")
        GlobalScope.launch {
            storageCopyUriLiveData.postValue(logRepository.getShareableCopyUri())
        }
    }

    fun changeCategoriesQuery(query: String) {
        queryLiveData.postValue(query)
    }
}