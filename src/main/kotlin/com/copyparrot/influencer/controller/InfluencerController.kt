package com.copyparrot.influencer.controller

import com.copyparrot.common.response.BaseResponse
import com.copyparrot.influencer.dto.CreateInfluencerReq
import com.copyparrot.influencer.dto.CreateInfluencerRes
import com.copyparrot.influencer.dto.InfluencerDto
import com.copyparrot.influencer.service.InfluencerService
import com.fasterxml.jackson.databind.ser.Serializers.Base
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/influencer")
class InfluencerController (
    val influencerService: InfluencerService
) {

    @PostMapping("")
    fun generageInfluencer(@Valid @RequestBody createInfluencerReq: CreateInfluencerReq) : Mono<BaseResponse<CreateInfluencerRes>> {
        return influencerService.generateInfluencer(createInfluencerReq)
            .map { res -> BaseResponse(data = res) }
    }

    @GetMapping("")
    fun getInfluencers() : Mono<BaseResponse<List<InfluencerDto>>> {
        return influencerService.getInfluencers()
            .map { influencerDto ->
                BaseResponse(data = influencerDto)
            }
    }

}