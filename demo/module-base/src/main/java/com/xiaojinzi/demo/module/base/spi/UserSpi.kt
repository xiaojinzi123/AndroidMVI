package com.xiaojinzi.demo.module.base.spi

import kotlinx.coroutines.flow.Flow

interface UserSpi {

    val userInfoStateOb: Flow<String>

}