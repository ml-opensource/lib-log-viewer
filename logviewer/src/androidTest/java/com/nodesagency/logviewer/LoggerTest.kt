package com.nodesagency.logviewer

import android.support.test.InstrumentationRegistry
import androidx.room.Room
import com.nodesagency.logviewer.data.database.DatabaseLogRepository
import com.nodesagency.logviewer.data.database.LogDatabase
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test

class LoggerTest {

    private lateinit var logRepository: DatabaseLogRepository

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getContext()
        val inMemoryDatabase = Room.inMemoryDatabaseBuilder(context, LogDatabase::class.java).build()
        logRepository = DatabaseLogRepository(context, inMemoryDatabase)

        Logger.initialize(context, logRepository)
    }

    @After
    fun tearDown() {
        logRepository.clear()
    }

    @Test
    fun logs_with_general_category_name_if_one_is_not_provided() {
        val message = "A message"
        Logger.log(message)

        val categories = logRepository.getAllCategoriesAlphabeticallySorted()
        val generalLogEntries = logRepository.getLogEntriesForCategory(categoryId = GENERAL_CATEGORY_ID)

        assertEquals(1, categories.size)
        assertEquals(1, generalLogEntries.size)
        assertEquals(message, generalLogEntries[0].message)
    }
}