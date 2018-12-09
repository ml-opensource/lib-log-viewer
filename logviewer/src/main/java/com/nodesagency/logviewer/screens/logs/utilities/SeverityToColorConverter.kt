package com.nodesagency.logviewer.screens.logs.utilities

import android.content.res.Resources
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import com.nodesagency.logviewer.CommonSeverityLevels
import com.nodesagency.logviewer.R

internal class SeverityToColorConverter(private val resources: Resources) {

    @ColorInt
    fun getColorForSeverityId(severityId: Long): Int? = when (severityId) {
        idFor(CommonSeverityLevels.VERBOSE) -> getColor(R.color.severity_gray)
        idFor(CommonSeverityLevels.INFO) -> getColor(R.color.severity_blue)
        idFor(CommonSeverityLevels.DEBUG) -> getColor(R.color.severity_green)
        idFor(CommonSeverityLevels.WARNING) -> getColor(R.color.severity_yellow)
        idFor(CommonSeverityLevels.ERROR) -> getColor(R.color.severity_red)
        idFor(CommonSeverityLevels.ASSERT) -> getColor(R.color.severity_dark_red)
        idFor(CommonSeverityLevels.WTF) -> getColor(R.color.severity_darkest_red)
        else -> null
    }

    private fun idFor(commonSeverityLevelItem: CommonSeverityLevels): Long = commonSeverityLevelItem.severity.id!! // id is always defined there

    @ColorInt
    private fun getColor(@ColorRes colorId: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            resources.getColor(colorId, null)
        } else {
            resources.getColor(colorId)
        }
    }
}