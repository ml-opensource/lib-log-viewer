package com.nodesagency.logviewer.data.database

import android.content.Context
import android.net.Uri
import androidx.paging.DataSource
import com.nodesagency.logviewer.data.LogRepository
import com.nodesagency.logviewer.data.model.Category
import com.nodesagency.logviewer.data.model.LogDetails
import com.nodesagency.logviewer.data.model.LogEntry
import com.nodesagency.logviewer.data.model.Severity
import com.nodesagency.logviewer.domain.Filter

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

    override fun deleteAllCategoriesAndLogs() {
        return database
            .categoryDao()
            .deleteAllCategories() // It's enough to delete the categories as they cascade-delete the logs too
    }

    override fun getLogDetails(logEntryId: Long): LogDetails {
        return database
            .logDetailsDao()
            .getLogDetails(logEntryId)
    }


    override fun getLogsFilteredBy(filter: Filter): DataSource.Factory<Int, LogEntry> {
        if (filter.query.isEmpty()) return getChronologicallySortedLogEntries(filter.category)
        return when (filter) {
            is Filter.ByMessage -> database.logEntryDao().getLogsWithMessage(filter.category, filter.toQuery())
            is Filter.ByTag -> database.logEntryDao().getLogsWithTag(filter.category, filter.toQuery())
            is Filter.BySeverity -> database.logEntryDao().getLogsWithSeverity(filter.category, filter.toQuery())
            is Filter.Disabled -> getChronologicallySortedLogEntries(filter.category)
        }
    }

    override fun getCategoriesByName(name: String): DataSource.Factory<Int, Category> {
        return when {
            name.isEmpty() ->  database.categoryDao().getAlphabeticallySortedCategories()
            else ->  database.categoryDao().getCategoriesByName("%$name%")
        }
    }

    override fun getShareableCopyUri(): Uri? {
        return LogDatabase.getDatabaseCopyUri(context)
    }
}
