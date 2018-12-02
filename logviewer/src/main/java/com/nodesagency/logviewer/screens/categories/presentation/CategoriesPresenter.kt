package com.nodesagency.logviewer.screens.categories.presentation

import com.nodesagency.logviewer.screens.categories.domain.CategoriesInteractor
import kotlinx.coroutines.*


internal class CategoriesPresenter(
    override val view: CategoriesPresentation.View,
    private val categoriesInteractor: CategoriesInteractor = CategoriesInteractor()
) : CategoriesPresentation.Presenter {

    private var jobs: MutableList<Job> = mutableListOf()
    private val uiScope = CoroutineScope(Dispatchers.Main)

    override fun onCategoriesRequested(skip: Long, limit: Long) {
        jobs.add(uiScope.launch {

            if (skip > 0) {
                view.showLoadingAtTheEndOfList()
            } else {
                view.showGeneralLoading()
            }

            val categories = categoriesInteractor.getCategories(skip, limit)

            if (isActive) {
                view.showAddedCategories(categories)
                view.hideLoading()
            }
        })
    }

    override fun onPresenterStopped() {
        super.onPresenterStopped()

        jobs
            .filter(Job::isActive)
            .forEach(Job::cancel)

        jobs.clear()
    }
}