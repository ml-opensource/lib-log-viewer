package com.nodesagency.logviewer.data.database

import android.content.Context
import android.util.Log
import androidx.paging.DataSource
import com.nodesagency.logviewer.concurrency.CoroutineScopeProvider
import com.nodesagency.logviewer.data.LogRepository
import com.nodesagency.logviewer.data.model.Category
import com.nodesagency.logviewer.data.model.LogDetails
import com.nodesagency.logviewer.data.model.LogEntry
import com.nodesagency.logviewer.data.model.Severity
import com.nodesagency.logviewer.domain.FilterState
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

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


    override fun getLogsFilteredBy(state: FilterState): DataSource.Factory<Int, LogEntry> {
        if (state.query.isEmpty()) return getChronologicallySortedLogEntries(state.category)
        return when (state) {
            is FilterState.ByMessage -> database.logEntryDao().getLogsWithMessage(state.category, state.toQuery())
            is FilterState.ByTag -> database.logEntryDao().getLogsWithTag(state.category, state.toQuery())
            is FilterState.BySeverity -> database.logEntryDao().getLogsWithSeverity(state.category, state.toQuery())
            is FilterState.Disabled -> getChronologicallySortedLogEntries(state.category)
        }
    }

    override fun getCategoriesByName(name: String): DataSource.Factory<Int, Category> {
        return when {
            name.isEmpty() ->  database.categoryDao().getAlphabeticallySortedCategories()
            else ->  database.categoryDao().getCategoriesByName("%$name%")
        }
    }
}
