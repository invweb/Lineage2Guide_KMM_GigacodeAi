package com.zx_tole.lineage2_guide.ui.common

import com.zx_tole.lineage2_guide.common.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel {

    protected val viewModelScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    // asStateFlow() уже есть в kotlinx.coroutines.flow, не нужен

    protected fun logError(message: String, throwable: Throwable? = null) {
        if (throwable != null) {
            Logger.e(throwable.message ?: message, throwable)
        } else {
            Logger.e(message)
        }
    }
}
