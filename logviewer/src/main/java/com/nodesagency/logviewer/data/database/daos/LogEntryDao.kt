package com.nodesagency.logviewer.data.database.daos

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nodesagency.logviewer.data.model.LogEntry

@Dao
internal interface LogEntryDao {

    @Query("SELECT * FROM LogEntries WHERE categoryId = :categoryId ORDER BY timestampMilliseconds")
    fun getChronologicallySortedLogEntries(categoryId: Long): DataSource.Factory<Int, LogEntry>

    @Query("SELECT * FROM LogEntries WHERE categoryId = :categoryId ORDER BY timestampMilliseconds")
    fun getAllLogEntries(categoryId: Long): List<LogEntry>

    @Insert
    fun insert(entry: LogEntry)

    @Query("SELECT * FROM LogEntries   INNER JOIN Severities sev ON severityId = sev.id WHERE (level LIKE :severityQuery) AND categoryId = :categoryId ORDER BY timestampMilliseconds")
    fun getLogsWithSeverity(categoryId: Long, severityQuery: String) : DataSource.Factory<Int, LogEntry>

    @Query("SELECT * FROM LogEntries WHERE (message LIKE :messageQuery) AND categoryId = :categoryId ORDER BY timestampMilliseconds")
    fun getLogsWithMessage(categoryId: Long, messageQuery: String) : DataSource.Factory<Int, LogEntry>

    @Query("SELECT * FROM LogEntries WHERE (tag LIKE :tagQuery)  AND categoryId = :categoryId ORDER BY timestampMilliseconds")
    fun getLogsWithTag(categoryId: Long, tagQuery: String) : DataSource.Factory<Int, LogEntry>

}