package com.nodesagency.logviewer.data

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import androidx.room.Room
import com.nodesagency.logviewer.data.database.DatabaseLogRepository
import com.nodesagency.logviewer.data.database.LogDatabase
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class DatabaseLogRepositoryTest {

    lateinit var repository: DatabaseLogRepository

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getContext()
        val database = Room.inMemoryDatabaseBuilder(context, LogDatabase::class.java).build()

        repository = DatabaseLogRepository(database)
    }

    @Test
    fun returns_empty_list_if_no_categories_are_stored() {
        val categories = repository.getAllCategoriesAlphabeticallySorted()

        assertEquals(0, categories.size)
    }

    @Test
    fun returns_all_stored_categories_alphabetically() {
        val categoryA = "categoryA"
        val categoryB = "categoryB"
        val categoryC = "categoryC"

        repository.insertCategory(categoryC)
        repository.insertCategory(categoryA)
        repository.insertCategory(categoryB)

        val categories = repository.getAllCategoriesAlphabeticallySorted()

        assertEquals(categoryA, categories[0].name)
        assertEquals(categoryB, categories[1].name)
        assertEquals(categoryC, categories[2].name)
    }
}