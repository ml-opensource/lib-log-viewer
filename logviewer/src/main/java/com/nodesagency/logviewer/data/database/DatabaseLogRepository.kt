package com.nodesagency.logviewer.data.database

import android.content.Context
import com.nodesagency.logviewer.data.GENERAL_CATEGORY_ID
import com.nodesagency.logviewer.data.GENERAL_CATEGORY_NAME
import com.nodesagency.logviewer.data.LogRepository
import com.nodesagency.logviewer.data.model.Category
import com.nodesagency.logviewer.data.model.LogEntry

internal class DatabaseLogRepository(
    private val context: Context,
    private val database: LogDatabase = LogDatabase.createDefault(context)
) : LogRepository {
    init {
        database
            .categoryDao()
            .insert(
                Category(
                    id = GENERAL_CATEGORY_ID,
                    name = GENERAL_CATEGORY_NAME
                )
            )
    }

    override fun getAllCategoriesAlphabeticallySorted(): List<Category> {
        return database
            .categoryDao()
            .getAllAlphabeticallySorted()
    }

    override fun insertCategory(categoryName: String) {
        database
            .categoryDao()
            .insert(Category(name = categoryName))
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
}