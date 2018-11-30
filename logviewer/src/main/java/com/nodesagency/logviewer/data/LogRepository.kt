package com.nodesagency.logviewer.data

import com.nodesagency.logviewer.data.model.Category
import com.nodesagency.logviewer.data.model.LogEntry


interface LogRepository {

    fun getAllCategoriesAlphabeticallySorted(): List<Category>

    /**
     * Inserts a category if the ID is null or replaces it if the ID already exists.
     *
     * @return the ID of the inserted category
     */
    fun put(category: Category): Long

    fun getLogEntriesForCategory(categoryId: Long): List<LogEntry>

    fun insertLogEntry(categoryId: Long, tag: String?, message: String)

    fun getIdForCategoryName(name: String): Long?

    fun clear()
}