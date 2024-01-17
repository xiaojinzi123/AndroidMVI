package com.xiaojinzi.androidmvi.demo.test

import androidx.annotation.Keep
import com.xiaojinzi.mvi.domain.BaseMVIUseCase
import com.xiaojinzi.mvi.domain.BaseMVIUseCaseImpl

@Keep
data class LoginUseCaseData(
    val name: String,
    val pass: String,
)

sealed class LoginIntent {

    data class NameChanged(
        val name: String,
    ) : LoginIntent()

}

interface LoginUseCase : BaseMVIUseCase<LoginIntent, LoginUseCaseData> {
}

class LoginUseCaseImpl(
    private val mviUseCase: BaseMVIUseCase<LoginIntent, LoginUseCaseData> = BaseMVIUseCaseImpl()
) : LoginUseCase, BaseMVIUseCase<LoginIntent, LoginUseCaseData> by mviUseCase {

    private fun nameChanged(
        intent: LoginIntent.NameChanged,
        data: LoginUseCaseData?,
    ): LoginUseCaseData? {
        return data?.copy(
            name = intent.name,
        )
    }

    override suspend fun intentProcess(
        intent: LoginIntent,
        currentData: LoginUseCaseData?
    ): LoginUseCaseData? {
        when (intent) {
            is LoginIntent.NameChanged -> {
                return nameChanged(intent, currentData)
            }
        }
    }


}

