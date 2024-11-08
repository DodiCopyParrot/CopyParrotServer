package com.copyparrot.shadowing.controller

import com.copyparrot.shadowing.dto.ShadowingReq
import com.copyparrot.shadowing.dto.ShadowingRes
import com.copyparrot.shadowing.service.ShadowingService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


@RestController
@RequestMapping("/shadowing")
class ShadowingController (
    val shadowingService: ShadowingService
) {

    @PostMapping(value = [""], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun shadowing(@RequestBody shadowingReq: ShadowingReq) : Flux<ShadowingRes> {
        return shadowingService.translateStream(shadowingReq.koText)
            .map { translatedText ->
                ShadowingRes(
                    enText = translatedText,
                    voiceFile = null
                )
            }
            .concatWith(
                shadowingService.generateVoiceFile(shadowingReq.koText)  // Generate the voice file after translation
                    .map { voiceFile ->
                        ShadowingRes(
                            enText = null,  // No more translation text
                            voiceFile = voiceFile  // Set the generated voice file
                        )
                    }
            )
    }
}