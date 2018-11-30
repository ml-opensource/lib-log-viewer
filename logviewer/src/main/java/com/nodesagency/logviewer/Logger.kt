package com.nodesagency.logviewer

import android.content.Context
import com.nodesagency.logviewer.data.GENERAL_CATEGORY_ID
import com.nodesagency.logviewer.data.GENERAL_CATEGORY_NAME
import com.nodesagency.logviewer.data.LogRepository
import com.nodesagency.logviewer.data.database.DatabaseLogRepository

object Logger {

    private var isInitialized = false
    private lateinit var logRepository: LogRepository

    fun initialize(
        context: Context,
        logRepository: LogRepository = createDefaultLogRepository(context)
    ) {
        this.logRepository = logRepository
        this.isInitialized = true
    }

    fun log(
        message: String,
        tag: String? = null,
        categoryName: String = GENERAL_CATEGORY_NAME
    ) {
        if (categoryName.isBlank()) {
            throw IllegalArgumentException("The name of the category shouldn't be blank")
        }

        val categoryId: Long? = logRepository.getIdForCategoryName(categoryName)

        logRepository.insertLogEntry(
            categoryId = categoryId ?: GENERAL_CATEGORY_ID,
            message = message,
            tag = tag
        )
    }

    private fun createDefaultLogRepository(context: Context): LogRepository {
        return DatabaseLogRepository(context)
    }
}