package com.nodesagency.logviewer.screens.logs.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.nodesagency.logviewer.R

internal class LogItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val lineNumberView: TextView
    private val tagView: TextView
    private val messageView: TextView

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

    init {
        LayoutInflater
            .from(context)
            .inflate(R.layout.view_log_list_item, this, true)

        lineNumberView = findViewById(R.id.lineNumberView)
        tagView = findViewById(R.id.tagView)
        messageView = findViewById(R.id.messageView)
    }
}