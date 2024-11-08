package com.copyparrot.common.exception.handler

import com.fasterxml.jackson.databind.JsonMappingException
import com.copyparrot.common.exception.CustomException
import com.copyparrot.common.exception.dto.FieldErrorDetail
import com.copyparrot.common.status.ResultCode
import org.slf4j.LoggerFactory
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes
import org.springframework.core.codec.DecodingException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.server.ServerWebInputException


@Component
class GlobalErrorAttributes : DefaultErrorAttributes() {

    private val logger = LoggerFactory.getLogger(GlobalErrorAttributes::class.java)

    override fun getErrorAttributes(request: ServerRequest, options: ErrorAttributeOptions) : Map<String, *> {
        var map = super.getErrorAttributes(request, options);
        val throwable: Throwable = getError(request)

        when(throwable) {
            is CustomException -> {
                val ex = getError(request) as CustomException
                map = generateExceptionMessage(map, ex, ex.resultCode)
            }
            is ServerWebInputException -> {
                val ex = getError(request) as ServerWebInputException
                val errors: MutableList<FieldErrorDetail> = ArrayList<FieldErrorDetail>()

                if(ex is WebExchangeBindException) {
                    ex.bindingResult.fieldErrors.map { fieldError ->
                        errors.add(
                            FieldErrorDetail(
                                field = fieldError.field,
                                message = fieldError.defaultMessage ?: "Invalid value"
                            ))
                    }
                }else if (ex.cause is DecodingException) {
                    val decodingException = ex.cause as DecodingException
                    val rootCause = decodingException.cause

                    if (rootCause is JsonMappingException) {
                        for (reference in rootCause.path) {
                            errors.add(
                                FieldErrorDetail(
                                    field = reference.fieldName ?: "unknown",
                                    message = "[${reference.fieldName}]가 누락되었습니다."
                                )
                            )
                        }
                    }
                }
                map = generateExceptionMessage(map, ex, ResultCode.INVALID_PARAMETER)
                map.put("errors", errors)
            }
            else -> {
                logger.error("Unhandled exception occurred", throwable)

                map["exception"] = throwable.javaClass.simpleName
                map["message"] = throwable.message ?: "Unexpected error occurred"
                map["status"] = HttpStatus.INTERNAL_SERVER_ERROR.value()
            }
        }

        return map;
    }

    private fun generateExceptionMessage(map: MutableMap<String, Any>, ex: Exception, resultCode: ResultCode) : Map<String, *> {
        map.put("exception", ex.javaClass.simpleName)
        map.put("error", resultCode.code)
        map.put("message", resultCode.message)
        map.put("status", HttpStatus.BAD_REQUEST.value())
        return map
    }
}