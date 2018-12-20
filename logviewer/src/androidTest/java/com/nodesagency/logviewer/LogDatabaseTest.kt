package com.nodesagency.logviewer

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import androidx.room.Room
import com.nodesagency.logviewer.data.database.LogDatabase
import com.nodesagency.logviewer.data.database.daos.CategoryDao
import com.nodesagency.logviewer.data.database.daos.LogEntryDao
import com.nodesagency.logviewer.data.database.daos.SeverityDao
import com.nodesagency.logviewer.data.model.Category
import com.nodesagency.logviewer.data.model.LogEntry
import com.nodesagency.logviewer.data.model.Severity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*

import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class LogDatabaseTest {


    private lateinit var logEntryDao: LogEntryDao
    private lateinit var caterDao: CategoryDao
    private lateinit var severityDao: SeverityDao

    private lateinit var db: LogDatabase


    @Before
    fun initilizeDatabase() {
        val context = InstrumentationRegistry.getContext()
        db = Room.inMemoryDatabaseBuilder(context, LogDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        logEntryDao = db.logEntryDao()
        caterDao = db.categoryDao()
        severityDao = db.severityDao()


    }

    @After
    @Throws(IOException::class)
    fun destroyDatabase() {
        db.close()
    }


    @Test
    fun testSeveritiesDao() {
        val severity = Severity(level = "Info")
        val id = severityDao.insert(severity)

        // Check if insertion is successful
        assertTrue(severityDao.getSeverityWithLevel("Info") != null)
        assertTrue(severityDao.getSeverityWithLevel("WTF") == null)
        assertTrue(id == severityDao.getSeverityWithLevel("Info")?.id)

        // Insert the same entity
        val replacedId = severityDao.insert(Severity(level = "Info"))

        // Data should be replaced with the same value, but new Id
        val replacedSeverity = severityDao.getSeverityWithLevel("Info")
        assertTrue(replacedSeverity != null)


        assertTrue(replacedId != id)

    }

    @Test
    fun testCategoriesDao() {

        // Test insertion
        val categoryId = caterDao.insert(Category(name = "I/O"))
        val category = caterDao.getCategoryWithName("I/O")
        assertTrue(category != null)
        assertEquals(categoryId, category?.id)

        val list = caterDao.getAlphabeticallySorted(0, 10)
        assertEquals(list.size, 1)

        repeat(3) {
            caterDao.insert(Category(name = "Category $it"))
        }

        assertEquals(caterDao.getAlphabeticallySorted(0, 10).size, 4)

        // Clear all categories
        caterDao.deleteAllCategories()

        assertTrue(caterDao.getAlphabeticallySorted(0, 10).isEmpty())

    }

    private fun getTestLog(message: String, category: String, severity: String): LogEntry {

        val severityId = severityDao.insert(Severity(level = severity))
        val categoryId = caterDao.insert(Category(name = category))
        return LogEntry(
            timestampMilliseconds = Date().time,
            categoryId = categoryId,
            severityId = severityId,
            message = message,
            stackTrace = null
        )
    }

    @Test
    fun testLogEntryDao() {
        val severityId = severityDao.insert(Severity(level = "Info"))
        val categoryId = caterDao.insert(Category(name = "Category"))
        val testLog =  LogEntry(
            timestampMilliseconds = Date().time,
            categoryId = categoryId,
            severityId = severityId,
            message = "Message",
            stackTrace = null
        )
        logEntryDao.insert(testLog)
        assertEquals(logEntryDao.getAllLogEntries(testLog.categoryId!!).size, 1)

    }


    @Test
    fun testLogEntriesMessageFiltering() {

        // Prepare test data
        val severityId = severityDao.insert(Severity(level = "Info"))
        val categoryId = caterDao.insert(Category(name = "Category"))
        repeat(10) {

            val message = when {
                it % 2 == 0 -> "message"
                it == 9 -> "Different String"
                else -> "MESSAGE CAPS"

            }

            logEntryDao.insert(
                LogEntry(
                timestampMilliseconds = Date().time,
                    categoryId = categoryId,
                    severityId = severityId,
                    message = message,
                    stackTrace = null

            ))
        }



        assertEquals(logEntryDao.getAllLogEntries(categoryId).size, 10)

        // Find all logs with containing "m" as a message
        var filtered = logEntryDao.getLogsListWithMessage(categoryId, "%m%")
        assertEquals(9, filtered.size)
        println(filtered.size)

        // Search result With uppercased "M" cuz querying is case insensetive
        filtered = logEntryDao.getLogsListWithMessage(categoryId, "%M%")
        assertEquals(9, filtered.size)


        filtered = logEntryDao.getLogsListWithMessage(categoryId, "%CAPS%")
        assertEquals(4, filtered.size)


        // Empty querry will return all entries
        filtered = logEntryDao.getLogsListWithMessage(categoryId, "%%")
        assertEquals(10, filtered.size)

    }

    @Test
    fun testLogEntriesTagFiltering() {

        // Prepare test data
        val severityId = severityDao.insert(Severity(level = "Info"))
        val categoryId = caterDao.insert(Category(name = "Category"))
        repeat(10) {

            val tag = when {
                it % 2 == 0 -> "tag"
                it == 9 -> "Different message"
                else -> "TAG CAPS"

            }

            logEntryDao.insert(
                LogEntry(
                    timestampMilliseconds = Date().time,
                    categoryId = categoryId,
                    severityId = severityId,
                    message = "Message",
                    tag = tag,
                    stackTrace = null

                ))
        }



        assertEquals(logEntryDao.getAllLogEntries(categoryId).size, 10)

        // Find all logs with containing "ta" substring as a tag
        var filtered = logEntryDao.getLogsListWithTag(categoryId, "%ta%")
        assertEquals(9, filtered.size)
        println(filtered.size)

        // Search result With uppercased "T" cuz querying is case insensetive
        filtered = logEntryDao.getLogsListWithTag(categoryId, "%TA%")
        assertEquals(9, filtered.size)


        filtered = logEntryDao.getLogsListWithTag(categoryId, "%CAPS%")
        assertEquals(4, filtered.size)


        // Empty query will return all entries
        filtered = logEntryDao.getLogsListWithTag(categoryId, "%%")
        assertEquals(10, filtered.size)

    }


    @Test
    fun testLogEntriesSeverityFiltering() {
        // Prepare test data

        val categoryId = caterDao.insert(Category(name = "Category"))

        val severityIds : Map<Long, String> = listOf("I/O", "Info", "Warnings",
            "Another Name", "different_name", "Value",
            "UPPERCASE", "lowercase", "12")
            .map {severityDao.insert(Severity(level = it)) to it }
            .toMap()


        repeat(20) {
            logEntryDao.insert(
                LogEntry(
                    timestampMilliseconds = Date().time,
                    categoryId = categoryId,
                    severityId = severityIds.keys.toList()[Random().nextInt(severityIds.size)],
                    message = "Message",
                    tag = "Tag",
                    stackTrace = null

                ))
        }

        // Compute expected value of logs with severity level that containts "i" substring
        val containsI = logEntryDao.getAllLogEntries(categoryId)
            .filter { severityIds[it.severityId]!!.contains("i", ignoreCase = true) }
            .size

        var actual = logEntryDao.getLogsListWithSeverity(categoryId, "%i%").size

        assertEquals(containsI, actual)

        // Compute expected value of logs with severity level that containts "case" substring
        val containsCase = logEntryDao.getAllLogEntries(categoryId)
            .filter { severityIds[it.severityId]!!.contains("case", ignoreCase = true) }
            .size

        actual = logEntryDao.getLogsListWithSeverity(categoryId, "%case%").size

        assertEquals(containsCase, actual)


        // Empty query returns all logs
        val all = logEntryDao.getAllLogEntries(categoryId).size
        actual = logEntryDao.getLogsListWithSeverity(categoryId, "%%").size
        assertEquals(all, actual)

    }



}