package com.nodesagency.logviewer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nodesagency.logviewer.data.model.Category
import com.nodesagency.logviewer.screens.categories.CategoriesFragment
import com.nodesagency.logviewer.screens.categories.OnCategorySelectListener
import com.nodesagency.logviewer.screens.log.LogFragment

private const val FRAGMENT_CATEGORIES = "categories"
private const val FRAGMENT_LOG = "log"

class LogViewerActivity : AppCompatActivity(), OnCategorySelectListener {

    companion object {
        fun createIntent(context: Context) = Intent(context, LogViewerActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_viewer)

        title = getString(R.string.title_log_categories)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, CategoriesFragment.newInstance(), FRAGMENT_CATEGORIES)
            .commit()
    }

    override fun onCategorySelected(category: Category) {
        supportFragmentManager
            .beginTransaction()
            .apply {
                supportFragmentManager.findFragmentByTag(FRAGMENT_CATEGORIES)?.let(::hide)
            }
            .addToBackStack(null)
            .add(R.id.fragmentContainer, LogFragment.newInstance(category))
            .commit()
    }
}
