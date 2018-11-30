package com.nodesagency.logviewer

import android.support.test.InstrumentationRegistry
import androidx.room.Room
import com.nodesagency.logviewer.data.database.DatabaseLogRepository
import com.nodesagency.logviewer.data.database.LogDatabase
import org.junit.Before
import org.junit.Test

class LoggerTest {

    private lateinit var logger: Logger

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getContext()
        val inMemoryDatabase = Room.inMemoryDatabaseBuilder(context, LogDatabase::class.java).build()
        val logRepository = DatabaseLogRepository(inMemoryDatabase)

        Logger.initialize(context, logRepository)
    }

    @Test
    fun logs_with_general_category_name_if_it_one_is_not_provided() {
//        val category =
//
//        Logger.log("Example message")
//
//        val getLog
    }
}