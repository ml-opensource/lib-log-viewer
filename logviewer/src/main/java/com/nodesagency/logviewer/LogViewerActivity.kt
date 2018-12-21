package com.nodesagency.logviewer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.nodesagency.logviewer.data.model.Category
import com.nodesagency.logviewer.screens.categories.CategoriesFragment
import com.nodesagency.logviewer.screens.categories.OnCategorySelectListener
import com.nodesagency.logviewer.screens.logdetails.LogDetailsFragment
import com.nodesagency.logviewer.screens.logs.LogsFragment
import com.nodesagency.logviewer.screens.logs.OnLogSelectListener

private const val FRAGMENT_CATEGORIES = "categories"
private const val FRAGMENT_LOGS = "logs"
private const val FRAGMENT_LOG_DETAILS = "log_details"
private const val STATE_TITLE = "state_title"

class LogViewerActivity : AppCompatActivity(),
    CategoriesFragment.CategoriesFragmentListener,
    OnLogSelectListener,
    FragmentManager.OnBackStackChangedListener {

    companion object {
        fun createIntent(context: Context) = Intent(context, LogViewerActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_viewer)

        if (savedInstanceState == null) {
            showCategoriesFragment()
        }

        title = savedInstanceState?.getString(STATE_TITLE) ?: getString(R.string.title_log_categories)
    }

    override fun onSaveInstanceState(outState: Bundle) {

        if (title != null && title.isNotBlank()) {
            outState.putString(STATE_TITLE, title.toString())
        } else {
            outState.remove(STATE_TITLE)
        }

        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()

        supportFragmentManager.addOnBackStackChangedListener(this)
    }

    override fun onPause() {
        super.onPause()

        supportFragmentManager.removeOnBackStackChangedListener(this)
    }

    override fun onCategorySelected(category: Category) {
        title = category.name

        supportFragmentManager
            .beginTransaction()
            .apply {
                supportFragmentManager.findFragmentByTag(FRAGMENT_CATEGORIES)?.let(::hide)
            }
            .addToBackStack(null)
            .add(R.id.fragmentContainerView, LogsFragment.newInstance(category))
            .commit()
    }

    override fun onLogSelected(logEntryId: kotlin.Long, severityId: Long) {
        val transaction = supportFragmentManager.beginTransaction()
        val previousDialog = supportFragmentManager.findFragmentByTag(FRAGMENT_LOG_DETAILS)

        if (previousDialog != null) {
            transaction.remove(previousDialog)
        }

        transaction.addToBackStack(null)

        val newDialog = LogDetailsFragment.newInstance(logEntryId, severityId)
        newDialog.show(transaction, FRAGMENT_LOG_DETAILS)
    }

    override fun onBackStackChanged() {
        val categoriesFragment = supportFragmentManager.findFragmentByTag(FRAGMENT_CATEGORIES)
        val areCategoriesShown = categoriesFragment != null && !categoriesFragment.isHidden

        if (areCategoriesShown) {
            title = getString(R.string.title_log_categories)
        }
    }

    private fun showCategoriesFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainerView, CategoriesFragment.newInstance(), FRAGMENT_CATEGORIES)
            .commit()
    }


}
