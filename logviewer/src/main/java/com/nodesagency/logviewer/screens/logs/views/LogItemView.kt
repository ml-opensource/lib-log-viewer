package com.nodesagency.logviewer.screens.logs.views

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import com.nodesagency.logviewer.R

internal class LogItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val lineNumberView: TextView
    private val tagView: TextView
    private val messageView: TextView

    private var selectableDrawable: Drawable? = null

    var lineNumber: Int
        get() = lineNumberView.text.toString().toInt()
        set(value) {
            lineNumberView.text = value.toString()
        }

    var tag: String?
        get() = tagView.text.toString()
        set(value) {
            tagView.text = value ?: ""
        }

    var message: String
        get() = messageView.text.toString()
        set(value) {
            messageView.text = value
        }

    @ColorInt
    var severityColor: Int = 0
    set(value) {
        field = value
        background = LayerDrawable(arrayOf(
            ColorDrawable(value),
            selectableDrawable
        ))
    }

    var screenshotVisible: Boolean = false
        set(value) {
            field = value
            if (field) tagView.setDrawableStart(R.drawable.ic_photo_black_24dp)
            else tagView.setDrawableStart(null)
        }

    init {
        LayoutInflater
            .from(context)
            .inflate(R.layout.view_log_list_item, this, true)

        lineNumberView = findViewById(R.id.lineNumberView)
        tagView = findViewById(R.id.tagView)
        messageView = findViewById(R.id.messageView)

        // Does not work when setting in xml merge tag
        isClickable = true
        isFocusable = true

        val outValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
        setBackgroundResource(outValue.resourceId)
        selectableDrawable = context.getDrawable(outValue.resourceId)
    }


    private fun TextView.setDrawableStart(resId: Int?) {
        val drawable = resId?.let { context.getDrawable(it).apply { bounds = Rect(0, 0, 60, 60) } }
        setCompoundDrawables(drawable, null, null, null)
}
}
