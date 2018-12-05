package com.nodesagency.logviewer.concurrency

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

/**
 * Used for shared scopes between the library's components
 */
internal object CoroutineScopeProvider {
    val ioScope = CoroutineScope(Dispatchers.IO)
}