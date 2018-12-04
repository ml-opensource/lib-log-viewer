package com.nodesagency.logviewer.screens.categories

import com.nodesagency.logviewer.data.model.Category

internal interface OnCategorySelectListener {
    fun onCategorySelected(category: Category)
}