package com.nodesagency.logviewer.data

import com.nodesagency.logviewer.data.model.Category
import com.nodesagency.logviewer.data.model.LogEntry

const val GENERAL_CATEGORY_ID = 0L
const val GENERAL_CATEGORY_NAME = "General"

interface LogRepository {

    fun getAllCategoriesAlphabeticallySorted(): List<Category>

    fun insertCategory(categoryName: String)

    fun getLogEntriesForCategory(categoryId: Long): List<LogEntry>

    fun insertLogEntry(categoryId: Long, tag: String?, message: String)

    fun getIdForCategoryName(name: String): Long?
}