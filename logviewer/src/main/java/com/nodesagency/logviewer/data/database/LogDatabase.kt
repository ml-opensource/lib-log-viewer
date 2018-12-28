package com.nodesagency.logviewer.data.database

import android.content.Context
import android.net.Uri
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nodesagency.logviewer.data.database.daos.CategoryDao
import com.nodesagency.logviewer.data.database.daos.LogDetailsDao
import com.nodesagency.logviewer.data.database.daos.LogEntryDao
import com.nodesagency.logviewer.data.database.daos.SeverityDao
import com.nodesagency.logviewer.data.model.Category
import com.nodesagency.logviewer.data.model.LogEntry
import com.nodesagency.logviewer.data.model.Severity

private const val DATABASE_NAME = "log_database"



@Database(entities = [Category::class, LogEntry::class, Severity::class], version = 2)
@TypeConverters(Converters::class)
internal abstract class LogDatabase : RoomDatabase() {

    companion object {

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE LogEntries ADD COLUMN screenshotUri Text")
            }
        }

        fun createDefault(context: Context): LogDatabase {
            return Room
                .databaseBuilder(context, LogDatabase::class.java, DATABASE_NAME)
                .addMigrations(MIGRATION_1_2)
                .build()
        }
    }

    abstract fun categoryDao(): CategoryDao

    abstract fun severityDao(): SeverityDao

    abstract fun logEntryDao(): LogEntryDao

    abstract fun logDetailsDao(): LogDetailsDao



}

internal class Converters {

    @TypeConverter
    fun fromUri(uri: String?) : Uri? {
        uri ?: return null
        return Uri.parse(uri)
    }

    @TypeConverter
    fun uriToString(uri: Uri?) : String? {
        return uri?.toString()
    }

}