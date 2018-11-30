package com.nodesagency.logviewer

import android.content.Context
import androidx.room.Room
import com.nodesagency.logviewer.data.LogRepository
import com.nodesagency.logviewer.data.database.DatabaseLogRepository
import com.nodesagency.logviewer.data.database.LogDatabase

object Logger {

    private lateinit var logRepository: LogRepository

    fun initialize(
        context: Context,
        logRepository: LogRepository = createDefaultLogRepository(context)
    ) {

    }

    private fun createDefaultLogRepository(context: Context): LogRepository {
        val database = Room.inMemoryDatabaseBuilder(context, LogDatabase::class.java).build()

        return DatabaseLogRepository(database)
    }


}