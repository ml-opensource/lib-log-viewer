package com.nodesagency.logviewer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Describes how important or serious the logged information is
 */
@Entity(tableName = "Severities")
data class Severity(
    /**
     * Generally you don't want to set the ID yourself, as it might have unforeseen consequences.
     */
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    /**
     * One-word description of the severity level, e.g. "Warning", "Error" etc.
     */
    val level: String
)