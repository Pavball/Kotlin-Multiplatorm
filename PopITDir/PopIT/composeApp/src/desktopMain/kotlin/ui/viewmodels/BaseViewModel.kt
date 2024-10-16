package ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.io.Closeable

internal abstract class BaseViewModel<T> : ViewModel(), Closeable {

    private val viewModelScope = CoroutineScope(
        Dispatchers.Default +
                SupervisorJob() +
                CoroutineExceptionHandler { coroutineContext, throwable ->
                    println("Exception in $this viewModelScope[$coroutineContext]: $throwable")
                }
    )

    protected val viewState = MutableSharedFlow<T?>(
        replay = 20,
        extraBufferCapacity = 6,
        onBufferOverflow = BufferOverflow.SUSPEND
    )

    //private val viewState = MutableStateFlow<T?>(null)

    protected fun runCommand(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(block = block)

    protected fun query(block: suspend CoroutineScope.() -> Flow<T>) =
        viewModelScope.launch {
            block()
                .collect { state ->
                    viewState.emit(state)
                }
        }

    override fun close() {
        viewModelScope.cancel("Closing ViewModel")
    }

    inline fun <reified T> viewState(): Flow<T> =
        viewState.filterNotNull().filterIsInstance<T>()
}


