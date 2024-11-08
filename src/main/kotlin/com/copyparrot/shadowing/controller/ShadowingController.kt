package com.copyparrot.shadowing.controller

import com.copyparrot.influencer.service.InfluencerService
import com.copyparrot.shadowing.dto.GenerateVoice
import com.copyparrot.shadowing.dto.ShadowingReq
import com.copyparrot.shadowing.dto.ShadowingRes
import com.copyparrot.shadowing.service.ShadowingService
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*


@RestController
@RequestMapping("/shadowing")
class ShadowingController (
    val shadowingService: ShadowingService,
    val influencerService: InfluencerService
) {

    @PostMapping(value = [""], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun shadowing(@RequestBody shadowingReq: ShadowingReq) : Flux<ShadowingRes> {
        return shadowingService.saveMark(shadowingReq)
            //Mark를 저장 후 Stream 조회
            .flatMapMany { savedMark ->
                shadowingService.translateStream(shadowingReq)
                    .map { translatedText ->
                        ShadowingRes(
                            markId = savedMark.id!!,
                            enText = translatedText
                        )
                    }
            }
    }

    @PostMapping(value = ["/generate-voice"], produces = ["audio/mpeg"])
    fun generateVoice(@RequestBody generateVoice: GenerateVoice): Mono<ResponseEntity<ByteArrayResource>> {
        val influencerMono = influencerService.getInfluencer(generateVoice.voiceId)

        return influencerMono.flatMap { influencer ->
            // generateVoiceFile 결과를 Flux<DataBuffer>로 가져옴
            shadowingService.generateVoiceFile(generateVoice, influencer.json)
                .collectList()
                .flatMap { dataBuffers ->
                    // 고유 파일 이름 생성
                    val uniqueFileName = "voice_${UUID.randomUUID()}.mp3"

                    // 파일을 S3에 업로드한 후 S3 URL 리턴
                    shadowingService.uploadToS3(uniqueFileName, dataBuffers)
                        .flatMap { s3Url ->
                            // 데이터베이스 업데이트 후 uniqueFileName 리턴
                            shadowingService.updatedMark(generateVoice.markId, generateVoice.enText, uniqueFileName)
                                .thenReturn(uniqueFileName)  // 업데이트 완료 후 파일 이름 반환
                        }
                }
                .flatMap { fileName ->
                    // 업로드 및 데이터베이스 업데이트 후 streamS3File 호출
                    shadowingService.streamS3File(fileName)
                }
        }
    }
}