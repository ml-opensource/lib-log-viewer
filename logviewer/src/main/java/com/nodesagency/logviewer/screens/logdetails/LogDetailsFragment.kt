package com.nodesagency.logviewer.screens.logdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nodesagency.logviewer.R

private const val ARGUMENT_LOG_ENTRY_ID = "log_entry_id"

class LogDetailsFragment : Fragment() {

    companion object {
        fun newInstance(logEntryId: Long) = LogDetailsFragment().apply {
            arguments = Bundle().apply {
                putLong(ARGUMENT_LOG_ENTRY_ID, logEntryId)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_log_details, container, false)
    }

}