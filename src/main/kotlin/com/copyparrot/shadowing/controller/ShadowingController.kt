package com.copyparrot.shadowing.controller

import com.copyparrot.common.response.BaseResponse
import com.copyparrot.influencer.repository.InfluencerRepository
import com.copyparrot.influencer.service.InfluencerService
import com.copyparrot.shadowing.dto.*
import com.copyparrot.shadowing.entity.Mark
import com.copyparrot.shadowing.service.ShadowingService
import com.copyparrot.users.repository.UsersRepository
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
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
        var lastTranslatedText: String? = null

        return shadowingService.saveMark(shadowingReq)
            //Mark를 저장 후 Stream 조회
            .flatMapMany { savedMark ->
                shadowingService.translateStream(shadowingReq)
                    .doOnNext { translatedText ->
                        lastTranslatedText = translatedText  // 각 번역 부분을 받을 때마다 변수에 저장
                    }
                    .map { translatedText ->
                        lastTranslatedText = translatedText
                        ShadowingRes(
                            markId = savedMark.id!!,
                            enText = translatedText,
                            end = false
                        )
                    }
                    .concatWith(Flux.just(ShadowingRes(
                        markId = savedMark.id!!,
                        enText = "<END>",
                        end = true
                    )))
            }
    }

    @PostMapping(value = ["/generate-voice"], produces = ["audio/mpeg"])
    fun generateVoice(@RequestBody generateVoice: GenerateVoice) : Mono<ResponseEntity<ByteArrayResource>> {
        return influencerService.getInfluencer(generateVoice.voiceId)
            .switchIfEmpty(Mono.error(IllegalArgumentException("Influencer not found")))
            .flatMap { influencer ->
                shadowingService.generateVoiceFile(generateVoice, influencer.json)
                    .collectList()
                    .flatMap { dataBuffers ->
                        val uniqueFileName = "voice_${UUID.randomUUID()}.mp3"
                        shadowingService.uploadToS3(uniqueFileName, dataBuffers)
                            .switchIfEmpty(Mono.error(IllegalArgumentException("Failed to upload file to S3")))
                            .flatMap { s3Url ->
                                shadowingService.updatedMark(generateVoice.markId, generateVoice.enText, uniqueFileName, influencer.voiceId)
                                    .thenReturn(uniqueFileName)
                            }
                    }
            }
            .flatMap { fileName ->
                shadowingService.streamS3File(fileName)
            }
    }

    @PostMapping("/record")
    fun recordVoice(@RequestBody recordVoice: RecordVoice) : Mono<BaseResponse<Mark>> {
        return shadowingService.voiceSave(recordVoice.markId)
            .map { res -> BaseResponse(data = res) }
    }

    @GetMapping("/record")
    fun getRecords(@RequestParam(name = "uuid") uuid: String) : Mono<BaseResponse<List<MarkDto>>> {
        return shadowingService.getMarks(uuid)
            .map { res -> BaseResponse(data = res) }
    }

    @PostMapping(value = ["/record/generate-voice"], produces = ["audio/mpeg"])
    fun listenRecordedVoice(@RequestBody listenVoice: ListenVoice) : Mono<ResponseEntity<ByteArrayResource>> {
        return shadowingService.streamS3File(listenVoice.file)
    }


}