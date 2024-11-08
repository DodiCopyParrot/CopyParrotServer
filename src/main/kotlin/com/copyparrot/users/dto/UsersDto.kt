package com.copyparrot.users.dto

import com.copyparrot.users.entity.Users
import com.copyparrot.util.TimeUtil.Companion.getCurrentTimeAsString
import jakarta.validation.constraints.NotBlank

class OnboardingUserReq (
    @field:NotBlank(message = "id는 필수 값입니다.")
    val uuid: String,
) {

    fun toEntity() : Users {
        return Users(
            uuid = uuid,
            createdDate = getCurrentTimeAsString()
        )
    }
}

class OnboardingUserRes (
    val id: String,
    val createdDate: String
)


