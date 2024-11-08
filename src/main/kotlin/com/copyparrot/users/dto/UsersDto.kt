package com.copyparrot.users.dto

import com.copyparrot.users.entity.Users
import com.copyparrot.util.TimeUtil.Companion.getCurrentTimeAsString
import jakarta.validation.constraints.NotBlank

class OnboardingUserReq (
    @field:NotBlank(message = "id는 필수 값입니다.")
    val uuid: String,
    val name: String?,
) {

    fun toEntity() : Users {
        return Users(
            uuid = uuid,
            name = name,
            createdDate = getCurrentTimeAsString()
        )
    }
}

class OnboardingUserRes (
    val id: String,
    val name: String?,
    val createdDate: String
)


