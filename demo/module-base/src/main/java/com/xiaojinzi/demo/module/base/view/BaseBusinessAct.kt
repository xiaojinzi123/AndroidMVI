package com.xiaojinzi.demo.module.base.view

import android.os.Bundle
import androidx.annotation.CallSuper
import com.xiaojinzi.component.Component
import com.xiaojinzi.mvi.view.BaseAct
import com.xiaojinzi.mvi.view.BaseViewModel

open class BaseBusinessAct<VM : BaseViewModel> : BaseAct<VM>() {

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Component.inject(this)
    }

}