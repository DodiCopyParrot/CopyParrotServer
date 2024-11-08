package com.copyparrot.users.controller

import com.copyparrot.common.response.BaseResponse
import com.copyparrot.users.dto.OnboardingUserReq
import com.copyparrot.users.dto.OnboardingUserRes
import com.copyparrot.users.service.UsersService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/users")
class UsersController (
    val usersService: UsersService
) {

    @PostMapping("")
    fun onboarding(@Valid @RequestBody onboardingUserReq: OnboardingUserReq) : Mono<BaseResponse<OnboardingUserRes>> {
        return usersService.onboarding(onboardingUserReq)
            .map { res -> BaseResponse(data = res) }
    }

}