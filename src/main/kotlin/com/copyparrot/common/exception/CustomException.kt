package com.copyparrot.common.exception

import com.copyparrot.common.status.ResultCode

open class CustomException(
    val resultCode: ResultCode
) : RuntimeException(resultCode.message)