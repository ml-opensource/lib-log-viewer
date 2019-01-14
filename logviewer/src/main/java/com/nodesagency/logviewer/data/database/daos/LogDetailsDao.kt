package com.nodesagency.logviewer.data.database.daos

import androidx.room.Dao
import androidx.room.Query
import com.nodesagency.logviewer.data.model.LogDetails

@Dao
internal interface LogDetailsDao {

    @Query("""
        SELECT
            timestampMilliseconds,
            tag,
            Categories.name AS category,
            Severities.level as severity,
            message,
            stackTrace,
            screenshotUri
        FROM
            Categories INNER JOIN LogEntries INNER JOIN Severities
        WHERE
            LogEntries.id = :logEntryId AND
            Categories.id = categoryId AND
            Severities.id = severityId
    """)
    fun getLogDetails(logEntryId: Long): LogDetails

}