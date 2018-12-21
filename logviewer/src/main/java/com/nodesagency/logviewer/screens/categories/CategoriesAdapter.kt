package com.nodesagency.logviewer.screens.categories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nodesagency.logviewer.R
import com.nodesagency.logviewer.data.model.Category
import kotlinx.android.synthetic.main.view_category_name.view.*


internal class CategoriesAdapter : PagedListAdapter<Category, CategoriesAdapter.ViewHolder>(
    DiffCallback()
) {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView : TextView = view.textView
        val pinnedButton : ImageButton = view.categoryPinnedBtn
    }

    var onCategorySelectListener: OnCategorySelectListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val textView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.view_category_name, parent, false)

        return ViewHolder(textView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    private fun ViewHolder.bind(category: Category) {
        nameTextView.text = category.name

        val imageRes = if (category.isPinned) R.drawable.ic_star else R.drawable.ic_star_border
        pinnedButton.setImageResource(imageRes)

        nameTextView.setOnClickListener {
            onCategorySelectListener?.onCategorySelected(category)
        }


        pinnedButton.setOnClickListener {
            onCategorySelectListener?.onCategoryPinButtonPressed(category)
        }

    }
}


private class DiffCallback : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Category, newItem: Category) = oldItem == newItem
}
