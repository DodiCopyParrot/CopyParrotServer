package com.copyparrot.influencer.service

import com.copyparrot.influencer.dto.CreateInfluencerReq
import com.copyparrot.influencer.dto.CreateInfluencerRes
import com.copyparrot.influencer.dto.InfluencerDto
import reactor.core.publisher.Mono

interface InfluencerService {

    fun generateInfluencer(createInfluencerReq: CreateInfluencerReq) : Mono<CreateInfluencerRes>

    fun getInfluencers() : Mono<List<InfluencerDto>>
}