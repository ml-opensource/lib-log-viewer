package com.nodesagency.logviewer

import android.content.Context
import com.nodesagency.logviewer.data.LogRepository
import com.nodesagency.logviewer.data.database.DatabaseLogRepository
import com.nodesagency.logviewer.data.model.Category
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal const val GENERAL_CATEGORY_ID = 0L
internal const val GENERAL_CATEGORY_NAME = "General"

object Logger {

    private var isInitialized = false
    private lateinit var logRepository: LogRepository
    private val uiScope = CoroutineScope(Dispatchers.Default)

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
    ) = doAfterInitializationCheck {
        uiScope.launch {
            val categoryId = logRepository.getIdForCategoryName(categoryName) ?: insertNewCategory(categoryName)

            logRepository.insertLogEntry(
                categoryId = categoryId,
                message = message,
                tag = tag
            )
        }
    }

    fun getRepository(): LogRepository = doAfterInitializationCheck { logRepository }

    private fun <T> doAfterInitializationCheck(doAfter: () -> T): T {
        if (!isInitialized) {
            throw IllegalStateException(
                "You need to call Logger.initialize(...) before using the class. " +
                        "It is recommended to do it during your application's onCreate()."
            )
        }

        return doAfter()
    }

    private fun insertNewCategory(categoryName: String): Long {
        if (categoryName.isBlank()) {
            throw IllegalArgumentException("The name of the category shouldn't be blank")
        }

        return if (categoryName == GENERAL_CATEGORY_NAME) {
            logRepository.put(Category(id = GENERAL_CATEGORY_ID, name = GENERAL_CATEGORY_NAME))
        } else {
            logRepository.put(Category(name = categoryName))
        }
    }

    private fun createDefaultLogRepository(context: Context): LogRepository {
        return DatabaseLogRepository(context)
    }
}