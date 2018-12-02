package com.nodesagency.logviewer.screens.common

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.nodesagency.logviewer.data.model.Category

internal interface Presentation {

    interface Presenter<V : View> : LifecycleObserver {

        val view: V

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun onPresenterStarted() {
            // Do nothing
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun onPresenterStopped() {
            // Do nothing
        }
    }

    interface View {

        fun showLoadingAtTheEndOfList()

        fun showGeneralLoading()

        fun showAddedCategories(categories: List<Category>)

        fun hideLoading()

    }

}