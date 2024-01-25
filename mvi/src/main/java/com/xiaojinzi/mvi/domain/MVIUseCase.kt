package com.xiaojinzi.mvi.domain

import com.xiaojinzi.mvi.anno.IntentProcess
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.full.callSuspend
import kotlin.reflect.jvm.isAccessible

private class IntentMethodException(
    method: KCallable<*>,
    intentProcess: IntentProcess,
) : RuntimeException("方法 $method 必须是 suspend 标记的, 并且方法参数只能是一个, 类型必须是：${intentProcess.value.java}")

/**
 * 从 [BaseUseCase] 扩展了 [addIntent] 方法, 来实现 MVI 意图的唯一入口
 */
interface MVIUseCase : BaseUseCase {

    /**
     * 添加一个意图
     */
    fun addIntent(intent: Any)

}

open class MVIUseCaseImpl : BaseUseCaseImpl(), MVIUseCase {

    private val intentEvent: MutableSharedFlow<Any> = MutableSharedFlow(
        extraBufferCapacity = Int.MAX_VALUE,
    )

    private val intentProcessMethodMap = mutableMapOf<KClass<*>, KCallable<*>>()

    override fun addIntent(intent: Any) {
        intentEvent.tryEmit(
            value = intent,
        )
    }

    init {

        // 收集意图
        this@MVIUseCaseImpl::class
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
                runCatching {
                    intentProcessMethodMap.get(
                        key = intentEvent::class
                    )?.run {
                        this.isAccessible = true
                        this.callSuspend(
                            this@MVIUseCaseImpl, intentEvent
                        )
                    }
                }
                println("处理完毕意图：$intentEvent")
            }
            .launchIn(scope = scope)

    }

}
