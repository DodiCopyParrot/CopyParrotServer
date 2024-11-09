package com.copyparrot.influencer.controller

import com.copyparrot.common.response.BaseResponse
import com.copyparrot.influencer.dto.*
import com.copyparrot.influencer.service.InfluencerService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
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