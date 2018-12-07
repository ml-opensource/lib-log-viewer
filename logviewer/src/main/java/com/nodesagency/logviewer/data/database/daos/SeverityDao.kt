package com.nodesagency.logviewer.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nodesagency.logviewer.data.model.Severity

@Dao
internal interface SeverityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(severity: Severity): Long

    @Query("SELECT * FROM Severities WHERE level = :severityLevel LIMIT 1")
    fun getSeverityWithLevel(severityLevel: String): Severity?

}