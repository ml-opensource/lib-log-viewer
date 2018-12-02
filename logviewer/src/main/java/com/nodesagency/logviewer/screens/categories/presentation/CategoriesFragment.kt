package com.nodesagency.logviewer.screens.categories.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nodesagency.logviewer.R
import com.nodesagency.logviewer.data.model.Category

class CategoriesFragment : Fragment(), CategoriesPresentation.View {

    private lateinit var presenter: CategoriesPresentation.Presenter
    private lateinit var categoriesRecyclerView: RecyclerView
    private lateinit var categoriesAdapter: RecyclerView.Adapter<*>
    private lateinit var layoutManager: RecyclerView.LayoutManager

    private val dataset = mutableListOf<Category>()

    companion object {
        fun newInstance() = CategoriesFragment().apply {
            arguments = Bundle()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = CategoriesPresenter(this)

        lifecycle.addObserver(presenter)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_categories, container, false)

        layoutManager = LinearLayoutManager(context)
        categoriesAdapter = CategoriesAdapter(dataset)

        categoriesRecyclerView = view.findViewById<RecyclerView>(R.id.categoriesRecyclerView).apply {
            setHasFixedSize(true)
            layoutManager = layoutManager
            adapter = categoriesAdapter
        }

        return view
    }

    override fun onDestroy() {
        super.onDestroy()

        lifecycle.removeObserver(presenter)
    }

    override fun showLoadingAtTheEndOfList() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showGeneralLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showAddedCategories(categories: List<Category>) {
        dataset.addAll(categories)
    }

    override fun hideLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}