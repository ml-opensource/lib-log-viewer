package com.nodesagency.logviewer.data.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

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
    ), ForeignKey(
        entity = Severity::class,
        parentColumns = ["id"],
        childColumns = ["severityId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class LogEntry(
    /**
     * Generally you don't want to set the ID yourself, as it might have unforeseen consequences.
     */
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val timestampMilliseconds: Long,
    val categoryId: Long? = null,
    val severityId: Long,
    val message: String,
    val stackTrace: String?,
    val tag: String? = null,
    val screenshotUri: String?
) {

    fun toShareMessage() : String {
        return "LogEntry: \n" +
                "Message: $message\n" +
                "Tag: $tag\n" +
                "Timestamp: ${Date(timestampMilliseconds)}\n" +
                "Stack Trace: $stackTrace"
    }

}
