package com.nodesagency.logviewer.screens.logs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nodesagency.logviewer.Logger
import com.nodesagency.logviewer.R
import com.nodesagency.logviewer.data.model.Category
import com.nodesagency.logviewer.screens.logs.utilities.SeverityToColorConverter

private const val ARGUMENT_CATEGORY = "category"

internal class LogsFragment : Fragment() {
    private lateinit var logEntriesRecyclerView: RecyclerView

    private lateinit var logEntriesAdapter: LogEntriesAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var logEntriesViewModel: LogEntriesViewModel
    private lateinit var category: Category
    private lateinit var onLogSelectListener: OnLogSelectListener

    companion object {
        fun newInstance(category: Category) = LogsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARGUMENT_CATEGORY, category)
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        onLogSelectListener = context as? OnLogSelectListener
                ?: throw ClassCastException("Context must implement OnLogSelectListener.")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        category = getValidCategoryOrThrow(arguments)
        val categoryId = category.id!! // !! is safe because of getValidCategoryOrThrow()

        logEntriesViewModel = ViewModelProviders
            .of(this, LogEntriesViewModelFactory(categoryId, Logger.getRepository()))
            .get(LogEntriesViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_logs, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
       return when (item?.itemId) {
            R.id.logsCategoryShare -> {
                val shareMessage = "Logs from ${category.name} category\n" +
                        logEntriesViewModel.logEntryList.value?.joinToString("\n") {it.toShareMessage()}
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, shareMessage)
                    type = "text/plain"
                }
                startActivity(Intent.createChooser(sendIntent, "Share logs category"))

                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_logs, container, false)
        val resources = inflater.context.resources

        logEntriesRecyclerView = view.findViewById(R.id.logEntriesView)

        layoutManager = LinearLayoutManager(context)
        logEntriesAdapter = LogEntriesAdapter(SeverityToColorConverter(resources))

        logEntriesRecyclerView.layoutManager = layoutManager
        logEntriesRecyclerView.adapter = logEntriesAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logEntriesViewModel
            .logEntryList
            .observe(this, Observer(logEntriesAdapter::submitList))
    }

    override fun onResume() {
        super.onResume()

        logEntriesAdapter.onLogSelectListener = onLogSelectListener
    }

    override fun onPause() {
        super.onPause()

        logEntriesAdapter.onLogSelectListener = null
    }

    /**
     * A "valid" category in this case is a category also involving an ID
     */
    private fun getValidCategoryOrThrow(arguments: Bundle?): Category {
        val category = arguments?.getParcelable(ARGUMENT_CATEGORY) as? Category

        return if (category?.id == null) {
            throw IllegalArgumentException(
                "No valid category has been provided at fragment start. " +
                        "Please make sure your category has an ID and to instantiate this fragment with " +
                        "newInstance(<category>)"
            )
        } else {
            category
        }
    }
}