package com.nodesagency.logviewer.screens.categories.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nodesagency.logviewer.R
import com.nodesagency.logviewer.data.model.Category

class CategoriesAdapter(
    private val dataset: List<Category>
) : PagedListAdapter<Category, CategoriesAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(val nameTextView: TextView) : RecyclerView.ViewHolder(nameTextView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val textView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.view_category_name, parent, false)
            .let { it as TextView }

        return ViewHolder(textView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nameTextView.text = dataset[position].name
    }

}

private class DiffCallback : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category) = oldItem == newItem

    override fun areContentsTheSame(oldItem: Category, newItem: Category) = oldItem == newItem
}
