package com.xiaojinzi.mvi.support

import androidx.annotation.Keep
import androidx.annotation.StringRes

@Keep
internal data class StringItemDto(
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

internal val EmptyStringItemDto = StringItemDto(value = "")

internal fun @receiver:StringRes Int.toStringItemDto() = StringItemDto(valueRsd = this)

internal fun String.toStringItemDto() = StringItemDto(value = this)