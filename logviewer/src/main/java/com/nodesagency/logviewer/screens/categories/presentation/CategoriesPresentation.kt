package com.nodesagency.logviewer.screens.categories.presentation

import com.nodesagency.logviewer.screens.common.Presentation

internal interface CategoriesPresentation {

    interface Presenter : Presentation.Presenter<View> {

        fun onCategoriesRequested(skip: Long, limit: Long)

    }

    interface View : Presentation.View {

    }

}