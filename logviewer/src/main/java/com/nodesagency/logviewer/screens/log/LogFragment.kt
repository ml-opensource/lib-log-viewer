package com.nodesagency.logviewer.screens.log

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nodesagency.logviewer.R
import com.nodesagency.logviewer.data.model.Category
import kotlinx.android.synthetic.main.fragment_log.*

private const val ARGUMENT_CATEGORY = "category"

internal class LogFragment : Fragment() {

    companion object {
        fun newInstance(category: Category) = LogFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARGUMENT_CATEGORY, category)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_log, container, false)
    }

    override fun onResume() {
        super.onResume()

        val category = arguments?.getParcelable(ARGUMENT_CATEGORY) as? Category

        if (category != null) {
            categoryNameTextView.text = category.name
        }
    }
}