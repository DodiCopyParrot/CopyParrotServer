package com.copyparrot.shadowing.service.impl

import com.copyparrot.shadowing.service.ShadowingService
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

@Service
class ShadowingServiceImpl : ShadowingService {

    override fun translateStream(text: String): Flux<String> {
        return Flux.just(
            "This is the first part of the translation.",
            "This is the second part.",
            "And here’s the final part of the translation."
        )
        .delayElements(Duration.ofSeconds(1))
    }

    override fun generateVoiceFile(text: String): Mono<String> {
        return Mono.just("voice파일!")
    }

}