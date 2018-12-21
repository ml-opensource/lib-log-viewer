package com.nodesagency.logviewer.screens.logdetails

import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.nodesagency.logviewer.Logger
import com.nodesagency.logviewer.R
import com.nodesagency.logviewer.screens.logs.utilities.SeverityToColorConverter
import kotlinx.android.synthetic.main.fragment_log_details.*
import android.view.Display
import android.content.Context.WINDOW_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import android.util.DisplayMetrics



private const val ARGUMENT_LOG_ENTRY_ID = "log_entry_id"
private const val ARGUMENT_SEVERITY_ID = "severity_id"

internal class LogDetailsFragment : DialogFragment() {

    private val logEntryId: Long
        get() = arguments?.getLong(ARGUMENT_LOG_ENTRY_ID)
            ?: throw IllegalStateException("No log entry ID found among arguments")

    private val severityId: Long
        get() = arguments?.getLong(ARGUMENT_SEVERITY_ID)
            ?: throw IllegalStateException("No severity ID found among arguments")

    private lateinit var viewModel: LogDetailsViewModel

    companion object {
        fun newInstance(logEntryId: Long, severityId: Long) = LogDetailsFragment().apply {
            arguments = Bundle().apply {
                putLong(ARGUMENT_LOG_ENTRY_ID, logEntryId)
                putLong(ARGUMENT_SEVERITY_ID, severityId)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val logRepository = Logger.getRepository()
        viewModel = ViewModelProviders
            .of(this, LogDetailsViewModelFactory(logRepository, logEntryId))
            .get(LogDetailsViewModel::class.java)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_log_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.logEntryLiveData.observe(this, Observer { logDetails ->
            logDetailsView.setLogDetails(logDetails)

            logDetailsShareBtn.setOnClickListener {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, logDetails.toShareMessage())
                    type = "text/plain"
                }
                startActivity(Intent.createChooser(sendIntent, getString(R.string.share_log_message)))
            }
        })
    }

    override fun onResume() {
        super.onResume()
        updateLayoutParameters()
        setBackgroundColor()
    }

    private fun updateLayoutParameters() {
        val params = dialog?.window?.attributes
        val metrics = DisplayMetrics()
        val wm = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        display.getMetrics(metrics)

        if (metrics.heightPixels > metrics.widthPixels) {
            params?.height = (metrics.heightPixels * 0.80).toInt()
            params?.width = ViewGroup.LayoutParams.MATCH_PARENT
        } else {
            params?.height = ViewGroup.LayoutParams.WRAP_CONTENT
            params?.width = (metrics.widthPixels * 0.80).toInt()
        }

        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

    private fun setBackgroundColor() {
        SeverityToColorConverter(resources)
            .getColorForSeverityId(severityId)
            ?.let { view?.setBackgroundColor(it) }
    }
}