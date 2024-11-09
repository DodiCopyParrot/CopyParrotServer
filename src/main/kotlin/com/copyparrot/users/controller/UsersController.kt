package com.copyparrot.users.controller

import com.copyparrot.common.response.BaseResponse
import com.copyparrot.influencer.dto.HomeDto
import com.copyparrot.influencer.dto.InfluencerDto
import com.copyparrot.influencer.dto.UpdateInfluencerReq
import com.copyparrot.influencer.service.InfluencerService
import com.copyparrot.users.dto.OnboardingUserReq
import com.copyparrot.users.dto.OnboardingUserRes
import com.copyparrot.users.service.UsersService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
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

    @GetMapping("/home")
    fun home(@RequestParam(name = "uuid") uuid: String) : Mono<BaseResponse<HomeDto>> {
        return usersService.home(uuid)
            .map { res -> BaseResponse(data = res) }
    }

    @PatchMapping("")
    fun updateInfluencer(@RequestBody updateInfluencerReq: UpdateInfluencerReq) : Mono<BaseResponse<HomeDto>> {
        return usersService.updateInfluencer(updateInfluencerReq)
            .map { res -> BaseResponse(data = res) }
    }

}