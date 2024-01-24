package com.xiaojinzi.mvi.domain

import android.widget.Toast
import androidx.annotation.Keep
import androidx.annotation.StringRes
import com.xiaojinzi.mvi.support.StringItemDto
import com.xiaojinzi.mvi.support.toStringItemDto
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

enum class TipType {
    Toast
}

@Keep
internal data class TipBean(
    val type: TipType = TipType.Toast,
    val toastLength: Int = Toast.LENGTH_SHORT,
    val content: StringItemDto,
)

/**
 * 定义了一些公共的逻辑,
 */
interface CommonUseCase : MVIBaseUseCase {

    fun postActivityFinishEvent()

    val isLoadingStateOb: Flow<Boolean>

    val tipEventOb: Flow<TipBean>

    val activityFinishEventOb: Flow<Unit>

    fun showLoading(isShow: Boolean)

    fun tip(tipBean: TipBean)

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

}

/**
 * 不可以被继承, 只能被创建的方式使用
 */
class CommonUseCaseImpl : MVIBaseUseCaseImpl(), CommonUseCase {

    /**
     * 是否是加载状态, 用于控制 View 层的 loading 动画
     */
    override val isLoadingStateOb = MutableStateFlow(false)

    /**
     * 提示
     */
    override val tipEventOb = MutableSharedFlow<TipBean>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override val activityFinishEventOb = MutableSharedFlow<Unit>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override fun showLoading(isShow: Boolean) {
        isLoadingStateOb.tryEmit(isShow)
    }

    override fun tip(tipBean: TipBean) {
        tipEventOb.tryEmit(tipBean)
    }

    override fun postActivityFinishEvent() {
        activityFinishEventOb.tryEmit(Unit)
    }

}
