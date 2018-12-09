package com.nodesagency.logviewer.screens.logdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.nodesagency.logviewer.Logger
import com.nodesagency.logviewer.R
import com.nodesagency.logviewer.concurrency.CoroutineScopeProvider
import com.nodesagency.logviewer.data.LogRepository
import com.nodesagency.logviewer.screens.logs.utilities.SeverityToColorConverter
import kotlinx.android.synthetic.main.fragment_log_details.*
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

private const val ARGUMENT_LOG_ENTRY_ID = "log_entry_id"
private const val ARGUMENT_SEVERITY_ID = "severity_id"

internal class LogDetailsFragment : DialogFragment() {

    private val logEntryId: Long
        get() = arguments?.getLong(ARGUMENT_LOG_ENTRY_ID)
            ?: throw IllegalStateException("No log entry ID found among arguments")

    private val severityId: Long
        get() = arguments?.getLong(ARGUMENT_SEVERITY_ID)
            ?: throw IllegalStateException("No severity ID found among arguments")

    private lateinit var logRepository: LogRepository

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

        logRepository = Logger.getRepository()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_log_details, container, false)
    }

    override fun onResume() {
        super.onResume()

        updateLayoutParameters()
        loadData()
        setBackgroundColor()
    }

    private fun updateLayoutParameters() {
        val params = dialog?.window?.attributes

        params?.width = ViewGroup.LayoutParams.MATCH_PARENT
        params?.height = ViewGroup.LayoutParams.WRAP_CONTENT

        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

    private fun loadData() {
        runBlocking {
            val logDetailsDeferred = CoroutineScopeProvider.ioScope.async {
                logRepository.getLogDetails(logEntryId)
            }

            val logDetails = logDetailsDeferred.await()

            if (!logDetailsDeferred.isCancelled) {
                logDetailsView.setLogDetails(logDetails)
            }
        }
    }

    private fun setBackgroundColor() {
        SeverityToColorConverter(resources)
            .getColorForSeverityId(severityId)
            ?.let { view?.setBackgroundColor(it) }
    }
}