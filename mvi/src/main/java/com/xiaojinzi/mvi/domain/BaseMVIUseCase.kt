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

sealed class MVIIntentWrap {
    data object RetryInit: MVIIntentWrap()
    sealed class MVIIntent: MVIIntentWrap()
}

/**
 * 业务逻辑类的基类
 */
interface BaseMVIUseCase<MVIData> {

    val dataState: Flow<MVIData?>

    /**
     * 添加一个意图
     */
    fun addIntent(intent: MVIIntentWrap)

    /**
     * 处理意图
     */
    @Throws(Exception::class)
    suspend fun intentProcess(
        intent: MVIIntentWrap.MVIIntent,
        previousData: MVIData?,
    ): MVIData?

    fun destroy()

}

class BaseMVIUseCaseImpl<MVIData>(
    initData: MVIData? = null,
) : BaseMVIUseCase<MVIData> {

    private val scope = MainScope()

    private val intentEvent: MutableSharedFlow<MVIIntentWrap> = MutableSharedFlow(
        extraBufferCapacity = Int.MAX_VALUE,
    )

    final override val dataState: Flow<MVIData?> = MutableStateFlow(value = initData)

    final override fun addIntent(intent: MVIIntentWrap) {
        intentEvent.tryEmit(
            value = intent,
        )
    }

    override suspend fun intentProcess(
        intent: MVIIntentWrap.MVIIntent,
        previousData: MVIData?
    ): MVIData? {
        return previousData
    }

    override fun destroy() {
        scope.cancel()
    }

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
                    is MVIIntentWrap.RetryInit -> {

                    }
                    is MVIIntentWrap.MVIIntent -> {
                        val currentData = dataState.firstOrNull()
                        val newData = intentProcess(
                            intent = it,
                            previousData = currentData,
                        )
                    }
                }
            }
            .launchIn(scope = scope)
    }

}
