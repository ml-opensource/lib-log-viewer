package com.nodesagency.logviewer.data.database

import android.content.Context
import androidx.paging.DataSource
import com.nodesagency.logviewer.data.LogRepository
import com.nodesagency.logviewer.data.model.Category
import com.nodesagency.logviewer.data.model.LogEntry
import com.nodesagency.logviewer.data.model.Severity

internal class DatabaseLogRepository(
    private val context: Context,
    private val database: LogDatabase = LogDatabase.createDefault(context)
) : LogRepository {

    override fun getAlphabeticallySortedCategories(): DataSource.Factory<Int, Category> {
        return database
            .categoryDao()
            .getAlphabeticallySortedCategories()
    }

    override fun getChronologicallySortedLogEntries(categoryId: Long): DataSource.Factory<Int, LogEntry> {
        return database
            .logEntryDao()
            .getChronologicallySortedLogEntries(categoryId)
    }

    override fun put(category: Category): Long {
        return database
            .categoryDao()
            .insert(category)
    }


    override fun put(severity: Severity): Long {
        return database
            .severityDao()
            .insert(severity)
    }

    override fun put(logEntry: LogEntry) {
        database
            .logEntryDao()
            .insert(logEntry)
    }

    override fun getLogEntriesForCategoryId(categoryId: Long): List<LogEntry> {
        return database
            .logEntryDao()
            .getAllLogEntries(categoryId)
    }

    override fun getIdForCategoryName(name: String): Long? {
        return database
            .categoryDao()
            .getCategoryWithName(name)
            ?.id
    }

    override fun getIdForSeverityLevel(severityLevel: String): Long? {
        return database
            .severityDao()
            .getSeverityWithLevel(severityLevel)
            ?.id
    }

    override fun clear() {
        return database.clearAllTables()
    }
}
