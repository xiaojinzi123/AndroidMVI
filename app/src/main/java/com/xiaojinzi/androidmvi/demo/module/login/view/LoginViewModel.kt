package com.xiaojinzi.androidmvi.demo.module.login.view

import com.xiaojinzi.mvi.view.MVIBaseViewModel
import com.xiaojinzi.androidmvi.demo.module.login.domain.LoginUseCase
import com.xiaojinzi.androidmvi.demo.module.login.domain.LoginUseCaseImpl

class LoginViewModel(
    private val useCase: LoginUseCase = LoginUseCaseImpl(),
): MVIBaseViewModel(), LoginUseCase by useCase