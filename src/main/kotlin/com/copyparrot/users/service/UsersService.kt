package com.copyparrot.users.service

import com.copyparrot.influencer.dto.HomeDto
import com.copyparrot.influencer.dto.InfluencerDto
import com.copyparrot.influencer.dto.UpdateInfluencerReq
import com.copyparrot.users.dto.OnboardingUserReq
import com.copyparrot.users.dto.OnboardingUserRes
import reactor.core.publisher.Mono

interface UsersService {

    fun onboarding(onboardingUserReq: OnboardingUserReq) : Mono<OnboardingUserRes>

    fun home(uuid: String) : Mono<HomeDto>

    fun updateInfluencer(updateInfluencerReq: UpdateInfluencerReq) : Mono<HomeDto>

}