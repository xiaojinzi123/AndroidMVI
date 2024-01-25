package com.xiaojinzi.mvi.template.domain

import android.widget.Toast
import androidx.annotation.Keep
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xiaojinzi.mvi.domain.BaseUseCaseImpl
import com.xiaojinzi.mvi.domain.MVIUseCase
import com.xiaojinzi.mvi.domain.MVIUseCaseImpl
import com.xiaojinzi.mvi.template.support.commonHandle
import com.xiaojinzi.mvi.template.view.CommonAlertDialog
import com.xiaojinzi.support.annotation.HotObservable
import com.xiaojinzi.support.annotation.NoError
import com.xiaojinzi.support.compose.util.clickableNoRipple
import com.xiaojinzi.support.ktx.MutableSharedStateFlow
import com.xiaojinzi.support.ktx.app
import com.xiaojinzi.support.ktx.contentWithContext
import com.xiaojinzi.support.ktx.launchIgnoreError
import com.xiaojinzi.support.ktx.nothing
import com.xiaojinzi.support.ktx.timeAtLeast
import com.xiaojinzi.support.ktx.tryFinishActivity
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

interface BusinessUseCase : MVIUseCase, CommonUseCase {

    @Keep
    enum class ViewState {
        STATE_INIT,
        STATE_LOADING,
        STATE_ERROR,
        STATE_SUCCESS,
    }

    /**
     * 页面状态
     */
    @HotObservable(HotObservable.Pattern.BEHAVIOR, isShared = true)
    val pageInitStateObservableDto: MutableSharedStateFlow<ViewState>

    /**
     * 初始化数据
     */
    @Throws(Exception::class)
    suspend fun initData()

    /**
     * 尝试初始化
     */
    fun retryInit()

    /**
     * 执行任务, 自带 Loading
     * 需要自己的 usecase 实现 [CommonUseCase] 接口
     */
    @NoError
    fun executeJobWithLoading(job: suspend () -> Unit)

    @Throws(Exception::class)
    suspend fun blockingExecuteJobWithLoading(job: suspend () -> Unit)

}

open class BusinessUseCaseImpl(
    private val commonUseCase: CommonUseCase = CommonUseCaseImpl(),
    private val mviUseCase: MVIUseCase = MVIUseCaseImpl(),
) : BaseUseCaseImpl(),
    BusinessUseCase,
    MVIUseCase by mviUseCase,
    CommonUseCase by commonUseCase {

    override val pageInitStateObservableDto =
        MutableSharedStateFlow(initValue = BusinessUseCase.ViewState.STATE_INIT)

    @Throws(Exception::class)
    override suspend fun initData() {
    }

    final override fun retryInit() {
        scope.launchIgnoreError {
            try {
                pageInitStateObservableDto.value = BusinessUseCase.ViewState.STATE_LOADING
                timeAtLeast {
                    initData()
                }
                pageInitStateObservableDto.emit(
                    value = BusinessUseCase.ViewState.STATE_SUCCESS
                )
            } catch (e: Exception) {
                // e.printStackTrace()
                pageInitStateObservableDto.emit(
                    value = BusinessUseCase.ViewState.STATE_ERROR
                )
            }
        }
    }

    @NoError
    override fun executeJobWithLoading(job: suspend () -> Unit) {
        scope.launchIgnoreError {
            blockingExecuteJobWithLoading(job = job)
        }.invokeOnCompletion { error ->
            error?.commonHandle()
        }
    }

    override suspend fun blockingExecuteJobWithLoading(job: suspend () -> Unit) {
        val commonUseCase = this as? CommonUseCase
        try {
            commonUseCase?.showLoading()
            timeAtLeast {
                job.invoke()
            }
        } catch (error: Exception) {
            error.commonHandle()
        } finally {
            commonUseCase?.hideLoading()
        }
    }

    override fun destroy() {
        super.destroy()
        commonUseCase.destroy()
    }

    init {
        retryInit()
    }

}

@Composable
inline fun <reified VM : ViewModel> BusinessContentView(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .nothing(),
    needInit: Boolean = true,
    contentAlignment: Alignment = Alignment.Center,
    noinline content: @Composable BoxScope.(vm: VM) -> Unit,
) {
    val context = LocalContext.current
    val vm: VM = viewModel()
    val viewState = when (vm) {
        is BusinessUseCase -> {
            val pageInitState by vm.pageInitStateObservableDto.collectAsState(initial = BusinessUseCase.ViewState.STATE_INIT)
            pageInitState
        }

        else -> {
            BusinessUseCase.ViewState.STATE_SUCCESS
        }
    }
    val dialogContent = when (vm) {
        is DialogUseCase -> {
            val dialogContent by vm.confirmDialogStateOb.collectAsState(initial = null)
            dialogContent
        }

        else -> {
            null
        }
    }
    var isLoading by remember {
        mutableStateOf(value = false)
    }
    // 对 ui 控制的一些监听
    LaunchedEffect(key1 = Unit) {
        when (vm) {
            is CommonUseCase -> {
                vm.activityFinishEventOb
                    .onEach {
                        context.tryFinishActivity()
                    }
                    .launchIn(scope = this)
                vm.isLoadingOb
                    .onEach { isShow ->
                        isLoading = isShow
                    }
                    .launchIn(scope = this)
                vm.tipEventOb
                    .onEach { tipContent ->
                        Toast.makeText(
                            app,
                            tipContent.content.contentWithContext(context = app),
                            tipContent.toastLength,
                        ).show()
                    }
                    .launchIn(scope = this)
            }
        }
    }
    Box(
        modifier = modifier,
        contentAlignment = contentAlignment,
    ) {
        if (needInit) {
            when (viewState) {
                BusinessUseCase.ViewState.STATE_INIT, BusinessUseCase.ViewState.STATE_LOADING -> {

                }

                BusinessUseCase.ViewState.STATE_ERROR -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickableNoRipple {
                                (vm as? BusinessUseCase)?.retryInit()
                            }
                            .nothing(),
                        contentAlignment = Alignment.Center,
                    ) {
                    }
                }

                BusinessUseCase.ViewState.STATE_SUCCESS -> {
                    content(vm)
                }
            }
        } else {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                content(vm)
            }
        }
    }
    dialogContent?.let {
        CommonAlertDialog(
            title = dialogContent.title,
            text = dialogContent.content,
            cancelText = dialogContent.negative,
            confirmText = dialogContent.positive,
            onDismissClick = {
                (vm as? BusinessUseCase)?.confirmDialogResultEventOb?.tryEmit(
                    value = DialogUseCase.ConfirmDialogResultType.CANCEL
                )
            },
        ) {
            (vm as? BusinessUseCase)?.confirmDialogResultEventOb?.tryEmit(
                value = DialogUseCase.ConfirmDialogResultType.CONFIRM
            )
        }
    }
}
