package com.xiaojinzi.androidmvi.demo.module.login.domain

import android.content.Context
import android.widget.Toast
import androidx.annotation.UiContext
import com.xiaojinzi.mvi.anno.IntentProcess
import com.xiaojinzi.mvi.template.domain.BusinessUseCase
import com.xiaojinzi.mvi.template.domain.BusinessUseCaseImpl
import com.xiaojinzi.support.annotation.StateHotObservable
import com.xiaojinzi.support.ktx.toStringItemDto
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

sealed class LoginIntent {

    data class Login(
        @UiContext val context: Context
    ) : LoginIntent()

}

interface LoginUseCase : BusinessUseCase {

    @StateHotObservable
    val nameStateOb: MutableStateFlow<String>

    @StateHotObservable
    val isNameErrorStateOb: Flow<Boolean>

    @StateHotObservable
    val passwordStateOb: MutableStateFlow<String>

    @StateHotObservable
    val isPasswordErrorStateOb: Flow<Boolean>

    @StateHotObservable
    val canSubmitStateOb: Flow<Boolean>

}

class LoginUseCaseImpl(
) : BusinessUseCaseImpl(), LoginUseCase {

    override val nameStateOb = MutableStateFlow(value = "")

    override val isNameErrorStateOb = nameStateOb.map {
        it.length < 6
    }

    override val passwordStateOb = MutableStateFlow(value = "")

    override val isPasswordErrorStateOb = passwordStateOb.map {
        it.isEmpty()
    }

    override val canSubmitStateOb = combine(
        nameStateOb,
        passwordStateOb,
    ) { name, password ->
        name.isNotBlank() && password.isNotBlank()
    }

    @BusinessUseCase.AutoLoading
    @IntentProcess(LoginIntent.Login::class)
    private suspend fun login(
        intent: LoginIntent.Login,
    ) {
        delay(1000)
        confirmDialogOrError(
            content = "确定要登录吗?".toStringItemDto(),
        )
        Toast.makeText(intent.context, "模拟登录成功", Toast.LENGTH_SHORT).show()
    }

    override suspend fun initData() {
        super.initData()
        delay(1000)
    }

}

