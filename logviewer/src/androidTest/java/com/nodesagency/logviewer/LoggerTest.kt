package com.nodesagency.logviewer

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import androidx.room.Room
import com.nodesagency.logviewer.data.LogRepository
import com.nodesagency.logviewer.data.database.DatabaseLogRepository
import com.nodesagency.logviewer.data.database.LogDatabase
import com.nodesagency.logviewer.data.model.Severity
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
    fun logs_with_general_category_name_by_default() = runBlocking {
        val message = "A message"
        val nonexistentIdException = IllegalStateException("General ID doesn't exist")

        Logger.log(message).join() // log() is run asynchronously, so we use join() to wait for it to finish

        val id = logRepository.getIdForCategoryName(GENERAL_CATEGORY_NAME) ?: throw nonexistentIdException
        val logEntriesForFoundId = logRepository.getLogEntriesForCategoryId(id)
        val generalLogEntries = logRepository.getLogEntriesForCategoryId(GENERAL_CATEGORY_ID)

        assertEquals(1, logEntriesForFoundId.size)
        assertEquals(1, generalLogEntries.size)
        assertEquals(message, generalLogEntries[0].message)
    }

    @Test
    fun logs_with_verbose_severity_by_default() = runBlocking {
        val message = "A message"
        val verboseSeverity = CommonSeverityLevels.VERBOSE.severity
        val nonexistentSeverityException = IllegalStateException("'Verbose' doesn't exist in DB")

        Logger.log(message).join() // log() is run asynchronously, so we use join() to wait for it to finish

        val id = logRepository.getIdForSeverityLevel(verboseSeverity.level) ?: throw nonexistentSeverityException
        val logEntries = logRepository.getLogEntriesForCategoryId(GENERAL_CATEGORY_ID)

        assertEquals(1, logEntries.size)
        assertEquals(verboseSeverity.id, logEntries[0].severityId)
    }

    @Test
    fun severity_extensions_store_proper_severities() = runBlocking {
        data class MessageWithSeverity(val message: String, val severity: Severity)

        val debug = MessageWithSeverity("Debug message", CommonSeverityLevels.DEBUG.severity)
        val error = MessageWithSeverity("Error message", CommonSeverityLevels.ERROR.severity)
        val info = MessageWithSeverity("Info message", CommonSeverityLevels.INFO.severity)
        val verbose = MessageWithSeverity("Verbose message", CommonSeverityLevels.VERBOSE.severity)
        val warning = MessageWithSeverity("Warning message", CommonSeverityLevels.WARNING.severity)
        val wtf = MessageWithSeverity("WTF message", CommonSeverityLevels.WTF.severity)

        Logger.apply {
            d(debug.message).join()
            e(error.message).join()
            i(info.message).join()
            v(verbose.message).join()
            w(warning.message).join()
            wtf(wtf.message).join()
        }

        val messages = logRepository.getLogEntriesForCategoryId(GENERAL_CATEGORY_ID)

        assertEquals(debug.severity.id, messages[0].severityId)
        assertEquals(error.severity.id, messages[1].severityId)
        assertEquals(info.severity.id, messages[2].severityId)
        assertEquals(verbose.severity.id, messages[3].severityId)
        assertEquals(warning.severity.id, messages[4].severityId)
        assertEquals(wtf.severity.id, messages[5].severityId)
    }

    private fun runDeinitialized(run: () -> Unit) {
        Logger.deinitialize()
        run()
    }
}