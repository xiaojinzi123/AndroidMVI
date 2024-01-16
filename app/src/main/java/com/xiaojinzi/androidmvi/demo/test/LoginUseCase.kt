package com.xiaojinzi.androidmvi.demo.test

import androidx.annotation.Keep
import com.xiaojinzi.mvi.domain.BaseMVIUseCase
import com.xiaojinzi.mvi.domain.BaseMVIUseCaseImpl
import com.xiaojinzi.mvi.domain.MVIIntentWrap

@Keep
data class LoginUseCaseData(
    val name: String,
    val pass: String,
)

data object Xxx: MVIIntentWrap.MVIIntent()

interface LoginUseCase: BaseMVIUseCase<LoginUseCaseData> {
}

class LoginUseCaseImpl(
    private val mviUseCase: BaseMVIUseCase<LoginUseCaseData> = BaseMVIUseCaseImpl()
): LoginUseCase, BaseMVIUseCase<LoginUseCaseData> by mviUseCase {

}

