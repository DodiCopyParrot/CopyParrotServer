package com.copyparrot.shadowing.service

import com.copyparrot.shadowing.dto.GenerateVoice
import org.springframework.core.io.buffer.DataBuffer
import reactor.core.publisher.Flux

interface ShadowingService {
    fun translateStream(text: String): Flux<String>

    fun generateVoiceFile(generateVoice: GenerateVoice, json: String): Flux<DataBuffer>
}