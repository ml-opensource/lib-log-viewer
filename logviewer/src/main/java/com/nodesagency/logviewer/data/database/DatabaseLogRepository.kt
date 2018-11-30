package com.nodesagency.logviewer.data.database

import android.content.Context
import com.nodesagency.logviewer.data.LogRepository
import com.nodesagency.logviewer.data.model.Category
import com.nodesagency.logviewer.data.model.LogEntry

internal class DatabaseLogRepository(
    private val context: Context,
    private val database: LogDatabase = LogDatabase.createDefault(context)
) : LogRepository {

    override fun getAllCategoriesAlphabeticallySorted(): List<Category> {
        return database
            .categoryDao()
            .getAllAlphabeticallySorted()
    }

    override fun getAlphabeticallySortedCategories(skip: Long, limit: Long): List<Category> {
        return database
            .categoryDao()
            .getAlphabeticallySorted(skip, limit)
    }

    override fun put(category: Category): Long {
        return database
            .categoryDao()
            .insert(category)
    }

    override fun getLogEntriesForCategory(categoryId: Long): List<LogEntry> {
        return database
            .logEntryDao()
            .getLogEntriesForCategory(categoryId)
    }

    override fun insertLogEntry(categoryId: Long, tag: String?, message: String) {
        val logEntryEntity = LogEntry(
            categoryId = categoryId,
            tag = tag,
            message = message
        )

        database
            .logEntryDao()
            .insert(logEntryEntity)
    }

    override fun getIdForCategoryName(name: String): Long? {
        return database
            .categoryDao()
            .getCategoryWithName(name)
            ?.id
    }

    override fun clear() {
        return database.clearAllTables()
    }
}
