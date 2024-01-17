package com.xiaojinzi.mvi.domain

import androidx.annotation.Keep
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

enum class MVIInitState {
    Initializing,
    Success,
    Error,
}

@Keep
data class MVIDataWrap<T>(
    val isShowLoading: Boolean = false,
    val initState: MVIInitState,
    val data: T,
)

sealed class MVIIntentInner {
    data object RetryInit : MVIIntentInner()
}

/**
 * 业务逻辑类的基类
 */
interface BaseMVIUseCase<MVIIntent : Any, MVIData> {

    val dataState: Flow<MVIData?>

    /**
     * 添加一个意图
     */
    fun addIntent(intent: MVIIntent)

    /**
     * Intent 的处理
     */
    suspend fun intentProcess(
        intent: MVIIntent,
        currentData: MVIData?,
    ): MVIData?

    fun destroy()

}

class BaseMVIUseCaseImpl<MVIIntent : Any, MVIData>(
    initData: MVIData? = null,
) : BaseMVIUseCase<MVIIntent, MVIData> {

    private val scope = MainScope()

    private val intentEvent: MutableSharedFlow<Any> = MutableSharedFlow(
        extraBufferCapacity = Int.MAX_VALUE,
    )

    override val dataState = MutableStateFlow(value = initData)

    override fun addIntent(intent: MVIIntent) {
        intentEvent.tryEmit(
            value = intent,
        )
    }

    override fun destroy() {
        scope.cancel()
    }

    override suspend fun intentProcess(
        intent: MVIIntent,
        currentData: MVIData?,
    ): MVIData? = currentData

    /**
     * 初始化数据
     */
    fun initData(
        previousData: MVIData,
    ): MVIData {
        return previousData
    }

    init {
        intentEvent
            .onEach {
                when (it) {
                    is MVIIntentInner.RetryInit -> {

                    }

                    else -> {
                        val currentData = dataState.firstOrNull()
                        runCatching {
                            @Suppress("UNCHECKED_CAST")
                            val newData = intentProcess(
                                intent = it as MVIIntent,
                                currentData = currentData,
                            )
                            dataState.emit(
                                value = newData
                            )
                        }
                    }
                }
            }
            .launchIn(scope = scope)
    }

}
