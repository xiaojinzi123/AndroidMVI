package com.xiaojinzi.mvi.template.support

import android.content.Context
import android.widget.Toast
import com.xiaojinzi.support.bean.StringItemDto
import com.xiaojinzi.support.init.AppInstance.app
import com.xiaojinzi.support.ktx.contentWithContext
import com.xiaojinzi.support.ktx.toStringItemDto

fun Throwable.getCommonHandleMessage(
    defStringRsd: Int? = null,
): StringItemDto? {

    var currentThrowable: Throwable = this

    do {

        when {

            currentThrowable is kotlinx.coroutines.CancellationException -> {
                return null
            } // ignore

            currentThrowable::class.java in listOf(
                java.net.SocketTimeoutException::class.java,
                java.net.UnknownHostException::class.java,
                javax.net.ssl.SSLHandshakeException::class.java,
                java.net.SocketException::class.java,
                java.net.ConnectException::class.java,
                javax.net.ssl.SSLProtocolException::class.java,
            ) -> {
                return "网络错误".toStringItemDto()
            }

            else -> {
                currentThrowable = currentThrowable.cause ?: break
            }

        }

    } while (true)

    return "未知错误".toStringItemDto()

}

/**
 * 错误的常见处理
 */
fun Throwable.commonHandle(
    context: Context = app,
    defStringRsd: Int? = null,
) {
    this.getCommonHandleMessage(
        defStringRsd = defStringRsd,
    )?.run {
        Toast.makeText(
            context,
            this.contentWithContext(),
            Toast.LENGTH_SHORT
        ).show()
    }
}