package com.nodesagency.logviewer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class LogViewerActivity : AppCompatActivity() {

    companion object {
        fun createIntent(context: Context) = Intent(context, LogViewerActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_viewer)
    }
}
