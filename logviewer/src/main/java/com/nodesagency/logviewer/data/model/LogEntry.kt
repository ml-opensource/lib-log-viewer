package com.nodesagency.logviewer.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.nodesagency.logviewer.data.GENERAL_CATEGORY_ID

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
    val categoryId: Long = GENERAL_CATEGORY_ID,
    val message: String,
    val tag: String? = null
)
