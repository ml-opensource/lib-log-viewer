package com.nodesagency.logviewer.domain

sealed class FilterState(
    val category: Long,
    query: String
) {

    var query: String = query
        get() = "%$field%"

    class Disabled(category: Long) : FilterState(category,"")
    class ByMessage(category: Long, query: String) : FilterState(category, query)
    class ByTag(category: Long, query: String) : FilterState(category, query)
    class BySeverity(category: Long, query: String) : FilterState(category, query)


}