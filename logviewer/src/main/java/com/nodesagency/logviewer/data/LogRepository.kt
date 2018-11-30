package com.nodesagency.logviewer.data

import com.nodesagency.logviewer.data.database.entities.Category

interface LogRepository {
    fun getAllCategoriesAlphabeticallySorted(): List<Category>

    fun insertCategory(categoryName: String)
}