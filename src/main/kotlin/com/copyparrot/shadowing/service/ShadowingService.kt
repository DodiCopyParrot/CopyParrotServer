package com.copyparrot.shadowing.service

import com.copyparrot.shadowing.dto.GenerateVoice
import com.copyparrot.shadowing.dto.MarkDto
import com.copyparrot.shadowing.dto.ShadowingReq
import com.copyparrot.shadowing.entity.Mark
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.ResponseEntity
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ShadowingService {

    fun saveMark(shadowingReq: ShadowingReq): Mono<Mark>

    fun updatedMark(markId: Long, enText: String, fileName: String, voiceId: Long) : Mono<Void>

    fun translateStream(shadowingReq: ShadowingReq): Flux<String>

    fun generateVoiceFile(generateVoice: GenerateVoice, json: String): Flux<DataBuffer>

    fun uploadToS3(fileName: String, dataBuffers: List<DataBuffer>): Mono<String>

    fun streamS3File(fileName: String): Mono<ResponseEntity<ByteArrayResource>>

    fun voiceSave(markId: Long) : Mono<Mark>

    fun getMarks(uuid: String) : Mono<List<MarkDto>>
}