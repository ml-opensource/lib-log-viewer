package com.nodesagency.logviewer

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import androidx.room.Room
import com.nodesagency.logviewer.data.LogRepository
import com.nodesagency.logviewer.data.database.DatabaseLogRepository
import com.nodesagency.logviewer.data.database.LogDatabase
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoggerTest {

    private lateinit var logRepository: LogRepository
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getContext()

        val inMemoryDatabase = Room.inMemoryDatabaseBuilder(context, LogDatabase::class.java).build()
        logRepository = DatabaseLogRepository(context, inMemoryDatabase)

        Logger.initialize(context, logRepository)
    }

    @After
    fun tearDown() {
        Logger.deinitialize()
        logRepository.clear()
    }

    @Test(expected = IllegalStateException::class)
    fun throws_exception_if_initialized_twice() = runDeinitialized {
        Logger.initialize(InstrumentationRegistry.getContext(), logRepository) // The first time should be successful
        Logger.initialize(InstrumentationRegistry.getContext(), logRepository) // The second time should fail
    }

    @Test(expected = IllegalStateException::class)
    fun throws_exception_if_log_is_called_prior_to_initialization() = runDeinitialized {
        Logger.log("Some message")
    }

    @Test
    fun logs_with_general_category_name_if_one_is_not_provided() = runBlocking {
        val message = "A message"
        Logger.log(message).join() // log() is run asynchronously, so we use join() to wait for it to finish

        val id = logRepository.getIdForCategoryName("General") ?: throw IllegalStateException("General ID doesn't exist")
        val categories = logRepository.getLogEntriesForCategory(id)
        val generalLogEntries = logRepository.getLogEntriesForCategory(categoryId = GENERAL_CATEGORY_ID)

        assertEquals(1, categories.size)
        assertEquals(1, generalLogEntries.size)
        assertEquals(message, generalLogEntries[0].message)
    }

    private fun runDeinitialized(run: () -> Unit) {
        Logger.deinitialize()
        run()
    }
}