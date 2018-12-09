package com.nodesagency.logviewer.screens.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nodesagency.logviewer.R
import com.nodesagency.logviewer.data.model.Category


internal class CategoriesAdapter : PagedListAdapter<Category, CategoriesAdapter.ViewHolder>(
    DiffCallback()
) {

    class ViewHolder(val nameTextView: TextView) : RecyclerView.ViewHolder(nameTextView)

    var onCategorySelectListener: OnCategorySelectListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val textView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.view_category_name, parent, false)
            .let { it as TextView }

        return ViewHolder(textView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    private fun ViewHolder.bind(category: Category) {
        nameTextView.text = category.name

        nameTextView.setOnClickListener {
            onCategorySelectListener?.onCategorySelected(category)
        }
    }
}


private class DiffCallback : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Category, newItem: Category) = oldItem == newItem
}
