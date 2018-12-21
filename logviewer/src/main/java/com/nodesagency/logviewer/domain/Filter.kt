package com.nodesagency.logviewer.domain

/**
 * Represents Filtering state used when querying for logs
 */
sealed class Filter(

    /**
     * ID of the category, filter will be applied for logs from this category
     */
    val category: Long,

    /**
     * query string that will be used for filtering
     */
    var query: String
) {


    fun toQuery() : String {
        return "%$query%"
    }

    /**
     * Represents disabled state
     * Repository is expected to return all logs from specific category
     */
    class Disabled(category: Long) : Filter(category,"")

    /**
     * Filters logs by Message
     * Repository is expected to return logs, whose messages contain a substring that matches query
     */
    class ByMessage(category: Long, query: String) : Filter(category, query)

    /**
     * Filters logs by Tag
     * Repository is expected to return logs, whose tags contain a substring that matches query
     */
    class ByTag(category: Long, query: String) : Filter(category, query)

    /**
     * Filters logs by Severity
     * Repository is expected to return logs, whose severity contain a substring that matches query
     */
    class BySeverity(category: Long, query: String) : Filter(category, query)


}