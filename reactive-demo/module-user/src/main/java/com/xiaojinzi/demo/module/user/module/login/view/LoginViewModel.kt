package com.xiaojinzi.demo.module.user.module.login.view

import com.xiaojinzi.demo.module.user.module.login.domain.LoginUseCase
import com.xiaojinzi.demo.module.user.module.login.domain.LoginUseCaseImpl
import com.xiaojinzi.mvi.view.BaseViewModel
import com.xiaojinzi.support.annotation.ViewLayer

@ViewLayer
class LoginViewModel(
    private val useCase: LoginUseCase = LoginUseCaseImpl(),
) : BaseViewModel(),
    LoginUseCase by useCase {
}