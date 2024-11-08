package com.copyparrot.common.status


enum class ResultCode (
    val code : String,
    var message : String
) {

    SUCCESS("D-0", "OK"),

    ERROR("D-99", "ERROR"),
    INVALID_PARAMETER("D-01", "입력값이 올바르지 않습니다."),
    NOT_FOUND("D-02", "일치하는 데이터가 없습니다."),
    ALREADY_DATA("D-03", "이미 데이터가 존재합니다."),
}