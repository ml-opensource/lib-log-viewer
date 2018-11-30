package com.nodesagency.logviewer.domain

import com.nodesagency.logviewer.Logger
import com.nodesagency.logviewer.data.LogRepository
import com.nodesagency.logviewer.data.model.Category

private const val NO_SKIP = 0L
private const val NO_LIMIT = -1L

class CategoriesInteractor(
    private val logRepository: LogRepository = Logger.getRepository()
) {
    suspend fun getCategories(skip: Long = NO_SKIP, limit: Long = NO_LIMIT): List<Category> {

        checkArgumentValidity(skip, limit)

        return if (skip == NO_SKIP && limit == NO_LIMIT) {
            logRepository.getAllCategoriesAlphabeticallySorted()
        } else {
            logRepository.getAlphabeticallySortedCategories(skip, limit)
        }
    }

    private fun checkArgumentValidity(skip: Long, limit: Long) {
        if (skip < 0 || (limit != NO_LIMIT && limit < 0)) {
            throw IllegalArgumentException("You cannot set the skip or limit below zero. ")
        }
    }
}