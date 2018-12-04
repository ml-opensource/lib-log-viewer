package com.nodesagency.logviewer.screens.categories

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nodesagency.logviewer.Logger
import com.nodesagency.logviewer.R

internal class CategoriesFragment : Fragment() {

    private lateinit var categoriesRecyclerView: RecyclerView
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var categoriesViewModel: CategoriesViewModel
    private lateinit var onCategorySelectListener: OnCategorySelectListener

    companion object {
        fun newInstance() = CategoriesFragment().apply {
            arguments = Bundle()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        onCategorySelectListener = context as? OnCategorySelectListener
                ?: throw ClassCastException("Context must implement OnCategorySelectListener.")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        categoriesViewModel = ViewModelProviders
            .of(this, CategoriesViewModelFactory(Logger.getRepository()))
            .get(CategoriesViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_categories, container, false)

        categoriesRecyclerView = view.findViewById<RecyclerView>(R.id.categoriesRecyclerView)
        val dividerDecoration = DividerItemDecoration(inflater.context, VERTICAL)

        layoutManager = LinearLayoutManager(context)
        categoriesAdapter = CategoriesAdapter()

        categoriesRecyclerView.adapter = categoriesAdapter
        categoriesRecyclerView.layoutManager = layoutManager
        categoriesRecyclerView.addItemDecoration(dividerDecoration)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoriesViewModel.categoryList.observe(this, Observer(categoriesAdapter::submitList))
    }

    override fun onResume() {
        super.onResume()

        categoriesAdapter.onCategorySelectListener = onCategorySelectListener
    }
}