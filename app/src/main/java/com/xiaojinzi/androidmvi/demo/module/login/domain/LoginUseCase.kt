package com.xiaojinzi.androidmvi.demo.module.login.domain

import com.xiaojinzi.mvi.anno.IntentProcess
import com.xiaojinzi.mvi.domain.MVIBaseUseCase
import com.xiaojinzi.mvi.domain.MVIBaseUseCaseImpl
import kotlinx.coroutines.flow.MutableStateFlow

sealed class LoginIntent {

    data object Login: LoginIntent()

}

interface LoginUseCase : MVIBaseUseCase {

    val nameStateOb: MutableStateFlow<String>

    val passwordStateOb: MutableStateFlow<String>

}

class LoginUseCaseImpl(
) : MVIBaseUseCaseImpl(), LoginUseCase {

    override val nameStateOb = MutableStateFlow(value = "")

    override val passwordStateOb = MutableStateFlow(value = "")

    @IntentProcess(LoginIntent.Login::class)
    private suspend fun login(
        intent: LoginIntent.Login,
    ) {

    }

}

