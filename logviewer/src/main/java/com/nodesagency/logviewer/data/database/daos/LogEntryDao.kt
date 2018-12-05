package com.nodesagency.logviewer.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nodesagency.logviewer.data.model.LogEntry

@Dao
internal interface LogEntryDao {

    @Query("SELECT * FROM LogEntries WHERE categoryId = :categoryId")
    fun getLogEntriesForCategory(categoryId: Long): List<LogEntry>

    @Insert
    fun insert(entry: LogEntry)

}