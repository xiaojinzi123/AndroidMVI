package com.xiaojinzi.androidmvi.demo.module.login.domain

import android.content.Context
import android.widget.Toast
import androidx.annotation.UiContext
import com.xiaojinzi.mvi.anno.IntentProcess
import com.xiaojinzi.mvi.domain.MVIIntentUseCase
import com.xiaojinzi.mvi.domain.MVIIntentUseCaseImpl
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

sealed class LoginIntent {

    data class Login(
        @UiContext val context: Context
    ) : LoginIntent()

}

interface LoginUseCase : MVIIntentUseCase {

    val nameStateOb: MutableStateFlow<String>

    val passwordStateOb: MutableStateFlow<String>

    val canSubmitStateOb: Flow<Boolean>

}

class LoginUseCaseImpl(
) : MVIIntentUseCaseImpl(), LoginUseCase {

    override val nameStateOb = MutableStateFlow(value = "")

    override val passwordStateOb = MutableStateFlow(value = "")

    override val canSubmitStateOb = combine(
        nameStateOb,
        passwordStateOb,
    ) { name, password ->
        name.isNotBlank() && password.isNotBlank()
    }

    @IntentProcess(LoginIntent.Login::class)
    private suspend fun login(
        intent: LoginIntent.Login,
    ) {
        delay(1000)
        Toast.makeText(intent.context, "模拟登录成功", Toast.LENGTH_SHORT).show()
    }

}

