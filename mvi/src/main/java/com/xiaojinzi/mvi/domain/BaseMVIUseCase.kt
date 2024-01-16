package com.xiaojinzi.mvi.domain

import androidx.annotation.Keep
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

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
    data object Retry
    sealed class MVIIntent
}

/**
 * 业务逻辑类的基类
 */
interface BaseMVIUseCase<MVIIntent: MVIIntentWrap.MVIIntent, MVIData> {

    val dataState: Flow<MVIData>

    /**
     * 添加一个意图
     */
    fun addIntent(intent: MVIIntent)

    /**
     * 处理意图
     */
    @Throws(Exception::class)
    suspend fun intentProcess(
        intent: MVIIntent,
        previousData: MVIData,
    ): MVIData

}

abstract class BaseMVIUseCaseImpl<MVIIntent: MVIIntentWrap.MVIIntent, MVIData>(
    initData: MVIData? = null,
): BaseMVIUseCase<MVIIntent, MVIData?> {

    final override val dataState: Flow<MVIData?> = MutableStateFlow(value = initData)

    final override fun addIntent(intent: MVIIntent) {

    }

    init {
        addIntent(intent = MVIIntentWrap.Retry)
    }

}
