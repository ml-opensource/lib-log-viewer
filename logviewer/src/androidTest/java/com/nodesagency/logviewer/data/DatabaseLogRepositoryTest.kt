package com.nodesagency.logviewer.data

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import androidx.room.Room
import com.nodesagency.logviewer.data.database.DatabaseLogRepository
import com.nodesagency.logviewer.data.database.LogDatabase
import com.nodesagency.logviewer.data.model.Category
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

        repository = DatabaseLogRepository(context, database)
    }

    @Test
    fun returns_no_categories_if_no_log_entries_have_been_recorded() {
        val categories = repository.getAllCategoriesAlphabeticallySorted()

        assertEquals(0, categories.size)
    }

    @Test
    fun returns_all_stored_categories_alphabetically() {
        val categoryAName = "categoryA"
        val categoryBName = "categoryB"
        val categoryCName = "categoryC"

        repository.put(Category(name = categoryCName))
        repository.put(Category(name = categoryAName))
        repository.put(Category(name = categoryBName))

        val categories = repository.getAllCategoriesAlphabeticallySorted()

        assertEquals(categoryAName, categories[0].name)
        assertEquals(categoryBName, categories[1].name)
        assertEquals(categoryCName, categories[2].name)
    }

    @Test
    fun returns_values_properly_when_skip_and_offset_are_applied() {
        val categoryNameFormat = "category_%d"
        val range = (0 until 10)
        val categoryNames = range.map { categoryNameFormat.format(it) }
        val skip = 2L
        val limit = 3L

        categoryNames.forEach { name ->
            repository.put(Category(name = name))
        }

        val allCategoriesCount = repository.getAllCategoriesAlphabeticallySorted().count()
        val skippedAndLimitedCategories = repository.getAlphabeticallySortedCategories(skip, limit)
        val expectedFirstCategoryName = categoryNameFormat.format(skip)
        val expectedLastCategoryName = categoryNameFormat.format(skip + limit - 1)

        assertEquals(range.count(), allCategoriesCount)
        assertEquals(limit, skippedAndLimitedCategories.count().toLong())
        assertEquals(expectedFirstCategoryName, skippedAndLimitedCategories.first().name)
        assertEquals(expectedLastCategoryName, skippedAndLimitedCategories.last().name)
    }


    @Test
    fun returns_all_existing_values_if_limit_is_too_high() {
        val categoryNameFormat = "category_%d"
        val skip = 0L
        val limit = 10L
        val actualValueCount = 3
        val categoryNames = (0 until actualValueCount).map { categoryNameFormat.format(it) }

        categoryNames.forEach { name ->
            repository.put(Category(name = name))
        }

        val allCategoriesCount = repository.getAllCategoriesAlphabeticallySorted().count()
        val skippedAndLimitedCategories = repository.getAlphabeticallySortedCategories(skip, limit)
        val expectedFirstCategoryName = categoryNameFormat.format(skip)
        val expectedLastCategoryName = categoryNameFormat.format(actualValueCount - 1)

        assertEquals(actualValueCount, allCategoriesCount)
        assertEquals(actualValueCount, skippedAndLimitedCategories.count())
        assertEquals(expectedFirstCategoryName, skippedAndLimitedCategories.first().name)
        assertEquals(expectedLastCategoryName, skippedAndLimitedCategories.last().name)
    }


    @Test
    fun clears_all_entries_properly() {
        val categoryAName = "categoryA"
        val categoryBName = "categoryB"
        val categoryCName = "categoryC"

        repository.put(Category(name = categoryCName))
        repository.put(Category(name = categoryAName))
        repository.put(Category(name = categoryBName))

        val storedCategories = repository.getAllCategoriesAlphabeticallySorted()

        repository.clear()

        val categoriesAfterRemoval = repository.getAllCategoriesAlphabeticallySorted()

        assertEquals(3, storedCategories.size)
        assertEquals(0, categoriesAfterRemoval.size)
    }

    @Test
    fun replaces_category_with_existing_id() {
        val oldCategory = Category(id = 0, name = "Old category")
        val newCategory = Category(id = 0, name = "New category")

        repository.put(oldCategory)
        repository.put(newCategory)

        val storedCategories = repository.getAllCategoriesAlphabeticallySorted()

        assertEquals(1, storedCategories.size)
        assertEquals(newCategory, storedCategories[0])
    }
}