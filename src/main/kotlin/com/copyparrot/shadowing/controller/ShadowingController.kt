package com.copyparrot.shadowing.controller

import com.copyparrot.influencer.service.InfluencerService
import com.copyparrot.shadowing.dto.GenerateVoice
import com.copyparrot.shadowing.dto.ShadowingReq
import com.copyparrot.shadowing.dto.ShadowingRes
import com.copyparrot.shadowing.service.ShadowingService
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux


@RestController
@RequestMapping("/shadowing")
class ShadowingController (
    val shadowingService: ShadowingService,
    val influencerService: InfluencerService
) {

    @PostMapping(value = [""], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun shadowing(@RequestBody shadowingReq: ShadowingReq) : Flux<ShadowingRes> {
        var lastTranslatedText: String? = null

        return shadowingService.translateStream(shadowingReq.koText)
            .doOnNext { translatedText ->
                lastTranslatedText = translatedText  // 각 번역 부분을 받을 때마다 변수에 저장
            }
            .map { translatedText ->
                ShadowingRes(enText = translatedText)
            }
    }

    @PostMapping(value = ["/generate-voice"], produces = ["audio/mpeg"])
    fun generateVoice(@RequestBody generateVoice: GenerateVoice): Flux<DataBuffer> {
        val influencerMono = influencerService.getInfluencer(generateVoice.voiceId)

        return influencerMono.flatMapMany { influencer ->
            shadowingService.generateVoiceFile(generateVoice, influencer.json)
        }
    }
}