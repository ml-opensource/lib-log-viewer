package com.nodesagency.logviewer

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nodesagency.logviewer.data.database.LogDatabase
import com.nodesagency.logviewer.data.database.daos.CategoryDao
import com.nodesagency.logviewer.data.database.daos.LogEntryDao
import com.nodesagency.logviewer.data.database.daos.SeverityDao
import com.nodesagency.logviewer.data.database.daos.SeverityDao_Impl
import com.nodesagency.logviewer.data.model.LogEntry
import com.nodesagency.logviewer.data.model.Severity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class LogDatabaseTest {


    private lateinit var logEntryDao: LogEntryDao
    private lateinit var caterDao: CategoryDao
    private lateinit var severityDao: SeverityDao

    private lateinit var db: LogDatabase

    private lateinit var severeties : MutableList<Severity>

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
        assert(severityDao.getSeverityWithLevel("Info") != null)
        assert(severityDao.getSeverityWithLevel("WTF") == null)
        assert(id == severityDao.getSeverityWithLevel("Info")?.id)

        // Insert the same entity
        val replacedId = severityDao.insert(Severity(level =  "Info"))

        // Data should be replaced with the same value, but new Id
        val replacedSeverity = severityDao.getSeverityWithLevel("Info")
        assert(replacedSeverity != null)


        assert(replacedId != id)

    }



    @Test
    fun testLogEntryInsert() {

        val severityId = severityDao.insert(Severity(level = "Info"))

    }

}