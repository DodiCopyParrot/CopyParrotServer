package com.copyparrot.shadowing.service

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ShadowingService {
    fun translateStream(text: String): Flux<String>

    fun generateVoiceFile(text: String): Mono<String>
}