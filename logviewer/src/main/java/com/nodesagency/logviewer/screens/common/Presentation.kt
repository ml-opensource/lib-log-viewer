package com.nodesagency.logviewer.screens.common

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

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

    interface View

}