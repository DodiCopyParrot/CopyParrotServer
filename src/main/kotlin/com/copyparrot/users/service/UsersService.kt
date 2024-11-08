package com.copyparrot.users.service

import com.copyparrot.users.dto.OnboardingUserReq
import com.copyparrot.users.dto.OnboardingUserRes
import reactor.core.publisher.Mono

interface UsersService {

    fun onboarding(onboardingUserReq: OnboardingUserReq) : Mono<OnboardingUserRes>

}