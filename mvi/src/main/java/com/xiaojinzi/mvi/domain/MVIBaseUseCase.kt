package com.xiaojinzi.mvi.domain

import androidx.annotation.CallSuper
import com.xiaojinzi.mvi.anno.IntentProcess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.suspendCoroutine
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.full.callSuspend
import kotlin.reflect.jvm.isAccessible

private class IntentMethodException(
    method: KCallable<*>,
    intentProcess: IntentProcess,
) : RuntimeException("方法 $method 必须是 suspend 标记的, 并且方法参数只能是一个, 类型必须是：${intentProcess.value.java}")

/**
 * 业务逻辑类的基类
 */
interface MVIBaseUseCase {

    /**
     * 添加一个意图
     */
    fun addIntent(intent: Any)

    /**
     * 销毁
     */
    fun destroy()

}

open class MVIBaseUseCaseImpl : MVIBaseUseCase {

    private val scope = MainScope()

    private val intentEvent: MutableSharedFlow<Any> = MutableSharedFlow(
        extraBufferCapacity = Int.MAX_VALUE,
    )

    private val intentProcessMethodMap = mutableMapOf<KClass<*>, KCallable<*>>()

    override fun addIntent(intent: Any) {
        intentEvent.tryEmit(
            value = intent,
        )
    }

    @CallSuper
    override fun destroy() {
        scope.cancel()
    }

    init {

        // 收集意图
        this@MVIBaseUseCaseImpl::class
            .members
            .forEach { method ->

                if (!method.isSuspend) {
                    return@forEach
                }

                val intentProcessAnno = method
                    .annotations
                    .find {
                        it is IntentProcess
                    } as? IntentProcess

                intentProcessAnno?.let {
                    if (method.parameters.size == 2) {
                        if (method.parameters[1].type.classifier != intentProcessAnno.value) {
                            throw IntentMethodException(
                                method = method,
                                intentProcess = intentProcessAnno,
                            )
                        }
                        intentProcessMethodMap[
                            intentProcessAnno.value
                        ] = method
                    } else {
                        throw IntentMethodException(
                            method = method,
                            intentProcess = intentProcessAnno,
                        )
                    }
                }

            }

        // 处理意图
        intentEvent
            .onEach { intentEvent ->
                println("准备处理意图：$intentEvent")
                intentProcessMethodMap.get(
                    key = intentEvent::class
                )?.run {
                    this.isAccessible = true
                    this.callSuspend(
                        this@MVIBaseUseCaseImpl, intentEvent
                    )
                }
                println("处理完毕意图：$intentEvent")
            }
            .launchIn(scope = scope)

    }

}
