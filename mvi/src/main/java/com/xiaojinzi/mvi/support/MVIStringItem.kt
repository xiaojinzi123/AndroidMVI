package com.xiaojinzi.mvi.support

import androidx.annotation.Keep
import androidx.annotation.StringRes

@Keep
data class MVIStringItem(
    @StringRes
    val valueRsd: Int? = null,
    val value: String? = null,
) {

    fun isEmpty(): Boolean {
        return valueRsd == null && value.isNullOrEmpty()
    }

    init {
        if (valueRsd == null && value == null) {
            throw IllegalArgumentException("valueRsd and value can not be null at the same time")
        }
    }

}

internal val EmptyStringItemDto = MVIStringItem(value = "")

internal fun @receiver:StringRes Int.toStringItemDto() = MVIStringItem(valueRsd = this)

internal fun String.toStringItemDto() = MVIStringItem(value = this)