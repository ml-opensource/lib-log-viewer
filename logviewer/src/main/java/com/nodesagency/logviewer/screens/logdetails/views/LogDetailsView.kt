package com.nodesagency.logviewer.screens.logdetails.views

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.nodesagency.logviewer.R
import com.nodesagency.logviewer.data.model.LogDetails
import java.text.SimpleDateFormat
import java.util.*

internal class LogDetailsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private val dateTimeView: TextView
    private val tagView: TextView
    private val severityView: TextView
    private val messageView: TextView
    private val stackTraceView: TextView
    private val screenshotImageView: ImageView

    init {
        LayoutInflater
            .from(context)
            .inflate(R.layout.view_log_details, this, true)

        dateTimeView = findViewById(R.id.dateTimeView)
        tagView = findViewById(R.id.tagView)
        severityView = findViewById(R.id.severityView)
        messageView = findViewById(R.id.messageView)
        stackTraceView = findViewById(R.id.stackTraceView)
        screenshotImageView = findViewById(R.id.screenshotImageView)

    }

    fun setLogDetails(logDetails: LogDetails) {
        logDetails.run {
            dateTimeView.text = timestampMilliseconds.formatTimestampInMilliseconds()
            tagView.text = tag
            severityView.text = severity
            messageView.text = message
            stackTraceView.text = stackTrace
            screenshotImageView.setImageURI(logDetails.screenshotUri)
            stackTraceView.visibility = if (stackTrace.isNullOrBlank()) View.GONE else { View.VISIBLE }
        }
    }
}

private fun Long.formatTimestampInMilliseconds(): String {
    val dateFormat = SimpleDateFormat("HH:mm:ss'\n'yyyy-MM-dd", Locale.getDefault())
    val date = Date(this)

    return dateFormat.format(date)
}
