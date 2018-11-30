package com.nodesagency.logviewer.data.database

import com.nodesagency.logviewer.data.LogRepository
import com.nodesagency.logviewer.data.database.entities.Category

internal class DatabaseLogRepository(private val database: LogDatabase) : LogRepository {

    override fun getAllCategoriesAlphabeticallySorted(): List<Category> {
        return database.categoryDao().getAllAlphabeticallySorted()
    }

    override fun insertCategory(categoryName: String) {
        database.categoryDao().insert(Category(name = categoryName))
    }
}