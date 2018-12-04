package com.nodesagency.logviewer

import android.content.Context
import androidx.annotation.VisibleForTesting
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
    private val ioScope = CoroutineScope(Dispatchers.IO)

    fun initialize(
        context: Context,
        logRepository: LogRepository = createDefaultLogRepository(context)
    ) {
        if (isInitialized) {
            throw IllegalStateException(
                "It seems that Logger has already been initialized somewhere else. You cannot call initialize() twice."
            )
        }

        this.logRepository = logRepository
        this.isInitialized = true
    }

    fun log(
        message: String,
        tag: String? = null,
        categoryName: String = GENERAL_CATEGORY_NAME
    ) = doAfterInitializationCheck {
        ioScope.launch {
            val categoryId = logRepository.getIdForCategoryName(categoryName) ?: insertNewCategory(categoryName)

            logRepository.insertLogEntry(
                categoryId = categoryId,
                message = message,
                tag = tag
            )
        }
    }

    internal fun getRepository(): LogRepository = doAfterInitializationCheck { logRepository }

    /**
     * WARNING: Do not use, only for testing!
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun deinitialize() {
        isInitialized = false
    }

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