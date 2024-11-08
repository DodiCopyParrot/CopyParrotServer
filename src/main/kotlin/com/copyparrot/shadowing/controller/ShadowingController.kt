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
    fun generateVoice(@RequestBody generateVoice: GenerateVoice): Flux<DataBuffer> {
        //인플루언서 조회 -> ai.play.ht 의 json파일을 가져오기 위해
        val influencerMono = influencerService.getInfluencer(generateVoice.voiceId)


        return influencerMono.flatMapMany { influencer ->
            // generateVoiceFile 결과를 변수에 저장
            val dataBufferFlux = shadowingService.generateVoiceFile(generateVoice, influencer.json).cache()

            // 클라이언트로 데이터 스트리밍
            dataBufferFlux.doOnTerminate {
                val uniqueFileName = "voice_${UUID.randomUUID()}.mp3"

                // 스트리밍이 완료된 후 S3 업로드와 DB 업데이트 비동기 실행
                dataBufferFlux.collectList()  // S3 업로드 시 전체를 모아야 하므로 collectList() 사용
                    .flatMap { dataBuffers ->
                        shadowingService.uploadToS3(uniqueFileName, dataBuffers)
                            .flatMap { s3Url ->
                                shadowingService.updatedMark(generateVoice.markId, generateVoice.enText, uniqueFileName)
                            }
                    }.subscribe()
            }
        }
    }
}