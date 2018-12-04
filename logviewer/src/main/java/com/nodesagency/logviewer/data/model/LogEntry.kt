package com.nodesagency.logviewer.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * The model for log entries. If [categoryId] is not specified, it will be part of the general log category.
 */
@Entity(
    tableName = "LogEntries",
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = ["id"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class LogEntry(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val timestampMilliseconds: Long = System.currentTimeMillis(),
    val categoryId: Long? = null,
    val message: String,
    val tag: String? = null
)
