package com.nodesagency.logviewer.data.database

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nodesagency.logviewer.BuildConfig
import com.nodesagency.logviewer.data.database.daos.CategoryDao
import com.nodesagency.logviewer.data.database.daos.LogDetailsDao
import com.nodesagency.logviewer.data.database.daos.LogEntryDao
import com.nodesagency.logviewer.data.database.daos.SeverityDao
import com.nodesagency.logviewer.data.model.Category
import com.nodesagency.logviewer.data.model.LogEntry
import com.nodesagency.logviewer.data.model.Severity
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

private const val DATABASE_NAME = "log_database"

@Database(entities = [Category::class, LogEntry::class, Severity::class], version = 1)
internal abstract class LogDatabase : RoomDatabase() {

    companion object {
        fun createDefault(context: Context): LogDatabase {
            return Room
                .databaseBuilder(context, LogDatabase::class.java, DATABASE_NAME)
                .build()
        }

        fun getDatabaseCopyUri(context: Context) : Uri? {
            val dbPath = context.getDatabasePath(DATABASE_NAME) ?: return null
            val dbCopy = File(context.filesDir, "database.db")
            val input = FileInputStream(dbPath).channel
            val output = FileOutputStream(dbCopy).channel
            input.transferTo(0, input.size(), output)
            input.close()
            output.close()
            return  FileProvider.getUriForFile(context, "${BuildConfig.APPLICATION_ID}.fileprovider", dbCopy)
        }
    }

    abstract fun categoryDao(): CategoryDao

    abstract fun severityDao(): SeverityDao

    abstract fun logEntryDao(): LogEntryDao

    abstract fun logDetailsDao(): LogDetailsDao

}