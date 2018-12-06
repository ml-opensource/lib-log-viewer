package com.nodesagency.logviewer.screens.log.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.nodesagency.logviewer.R

class LogItemView @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null
) : LinearLayout(context, attributes) {

    private val lineNumberView: TextView
    private val tagView: TextView
    private val messageView: TextView

    var lineNumber: String
        get() = lineNumberView.text.toString()
        set(value) {
            lineNumberView.text = value
        }

    var tag: String
        get() = tagView.text.toString()
        set(value) {
            tagView.text = value
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