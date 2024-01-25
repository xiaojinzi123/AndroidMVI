package com.xiaojinzi.mvi.template.domain

import android.widget.Toast
import androidx.annotation.Keep
import androidx.annotation.StringRes
import com.xiaojinzi.mvi.domain.MVIBaseUseCase
import com.xiaojinzi.mvi.domain.MVIBaseUseCaseImpl
import com.xiaojinzi.support.annotation.PublishHotObservable
import com.xiaojinzi.support.annotation.StateHotObservable
import com.xiaojinzi.support.bean.StringItemDto
import com.xiaojinzi.support.ktx.toStringItemDto
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

interface MVICommonDialogUseCase : MVIBaseUseCase {

    /**
     * 对话框的返回类型
     */
    enum class ConfirmDialogResultType {
        CONFIRM,
        CANCEL,
    }

    @Keep
    data class ConfirmDialogModel(
        val title: StringItemDto? = "提示".toStringItemDto(),
        val content: StringItemDto? = null,
        val negative: StringItemDto? = "取消".toStringItemDto(),
        val positive: StringItemDto? = "确定".toStringItemDto(),
    )

    /**
     * 显示确认对话框的
     */
    @StateHotObservable
    val confirmDialogStateOb: MutableStateFlow<ConfirmDialogModel?>

    /**
     * 确认的事件
     */
    @PublishHotObservable
    val confirmDialogResultEventOb: MutableSharedFlow<ConfirmDialogResultType>

}

class MVICommonDialogUseCaseImpl : MVIBaseUseCaseImpl(), MVICommonDialogUseCase {

    override val confirmDialogStateOb: MutableStateFlow<MVICommonDialogUseCase.ConfirmDialogModel?> =
        MutableStateFlow(value = null)

    override val confirmDialogResultEventOb: MutableSharedFlow<MVICommonDialogUseCase.ConfirmDialogResultType> =
        MutableSharedFlow(
            replay = 0,
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )

}

/**
 * 常用的一个业务 UseCase
 */
interface MVICommonUseCase : MVIBaseUseCase, MVICommonDialogUseCase {

    enum class TipType {
        Toast
    }

    @Keep
    data class TipBean(
        val type: TipType = TipType.Toast,
        val toastLength: Int = Toast.LENGTH_SHORT,
        val content: StringItemDto,
    )

    @StateHotObservable
    val isLoadingOb: Flow<Boolean>

    @PublishHotObservable
    val tipEventOb: Flow<TipBean>

    @PublishHotObservable
    val activityFinishEventOb: Flow<Unit>

    /**
     * 显示 loading
     */
    fun showLoading()

    /**
     * 隐藏 loading
     */
    fun hideLoading()

    /**
     * 提示
     */
    fun tip(value: TipBean)

    fun toast(@StringRes contentResId: Int, toastLength: Int = Toast.LENGTH_SHORT) {
        tip(
            TipBean(
                type = TipType.Toast,
                content = contentResId.toStringItemDto(),
                toastLength = toastLength,
            )
        )
    }

    fun toast(content: String, toastLength: Int = Toast.LENGTH_SHORT) {
        tip(
            TipBean(
                type = TipType.Toast,
                content = content.toStringItemDto(),
                toastLength = toastLength,
            )
        )
    }

    /**
     * 投递界面销毁的事件
     */
    fun postActivityFinishEvent()

}

class MVICommonUseCaseImpl(
    private val dialogUseCase: MVICommonDialogUseCase = MVICommonDialogUseCaseImpl(),
) : MVIBaseUseCaseImpl(),
    MVICommonUseCase,
    MVICommonDialogUseCase by dialogUseCase {

    override val isLoadingOb: MutableStateFlow<Boolean> = MutableStateFlow(false)

    override val tipEventOb: MutableSharedFlow<MVICommonUseCase.TipBean> = MutableSharedFlow(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override val activityFinishEventOb: MutableSharedFlow<Unit> = MutableSharedFlow(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override fun showLoading() {
        isLoadingOb.value = true
    }

    override fun hideLoading() {
        isLoadingOb.value = false
    }

    override fun tip(value: MVICommonUseCase.TipBean) {
        tipEventOb.tryEmit(value)
    }

    override fun postActivityFinishEvent() {
        activityFinishEventOb.tryEmit(Unit)
    }

    override fun destroy() {
        super.destroy()
        dialogUseCase.destroy()
    }

}
