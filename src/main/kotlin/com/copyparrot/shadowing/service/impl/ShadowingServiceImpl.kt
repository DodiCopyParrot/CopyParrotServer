package com.copyparrot.shadowing.service.impl

import com.copyparrot.shadowing.dto.GenerateVoice
import com.copyparrot.shadowing.service.ShadowingService
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import java.time.Duration

@Service
class ShadowingServiceImpl(
    @Value("\${elonmusk.id}")
    private val elonmuskId: String,
    @Value("\${elonmusk.key}")
    private val elonmuskKey: String,
    private val webClient: WebClient
) : ShadowingService {

    override fun translateStream(text: String): Flux<String> {

        return Flux.just(
            "This is the first part of the translation.",
            "This is the second part.",
            "And here’s the final part of the translation."
        )
        .delayElements(Duration.ofSeconds(1))
    }

    override fun generateVoiceFile(generateVoice: GenerateVoice, json: String): Flux<DataBuffer> {

        val requestBody = mapOf(
            "text" to generateVoice.enText,
            "voice_engine" to "Play3.0",
            "voice" to json,
            "output_format" to "mp3"
        )

        return webClient.post()
            .uri("https://api.play.ht/api/v2/tts/stream")
            .header("X-USER-ID", elonmuskId)
            .header("AUTHORIZATION", elonmuskKey)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .accept(MediaType.APPLICATION_OCTET_STREAM)
            .retrieve()
            .bodyToFlux(DataBuffer::class.java)
    }

}