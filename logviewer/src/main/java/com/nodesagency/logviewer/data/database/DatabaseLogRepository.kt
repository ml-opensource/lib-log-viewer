package com.nodesagency.logviewer.data.database

import com.nodesagency.logviewer.data.LogRepository
import com.nodesagency.logviewer.data.database.entities.Category

class DatabaseLogRepository(database: LogDatabase) : LogRepository {

    override fun getAllCategories(): List<Category> {
        return listOf()
    }

}