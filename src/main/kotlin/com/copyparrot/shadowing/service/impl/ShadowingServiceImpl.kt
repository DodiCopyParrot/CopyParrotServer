package com.copyparrot.shadowing.service.impl

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.GetObjectRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import com.copyparrot.common.exception.CustomException
import com.copyparrot.common.status.ResultCode
import com.copyparrot.shadowing.dto.GenerateVoice
import com.copyparrot.shadowing.dto.ShadowingReq
import com.copyparrot.shadowing.entity.Mark
import com.copyparrot.shadowing.repository.MarkRepository
import com.copyparrot.shadowing.service.ShadowingService
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.time.Duration

@Service
class ShadowingServiceImpl (
    @Value("\${elonmusk.id}")
    private val elonmuskId: String,
    @Value("\${elonmusk.key}")
    private val elonmuskKey: String,
    val amazonS3: AmazonS3,
    @Value("\${s3.bucketName}")
    val bucketName: String,
    @Value("\${bucket.path.image}")
    val bucketPath: String,
    private val webClient: WebClient,
    private val markRepository: MarkRepository
) : ShadowingService {

    override fun saveMark(shadowingReq: ShadowingReq): Mono<Mark> {
        return markRepository.save(Mark(
            uuid = shadowingReq.uuid,
            koText = shadowingReq.koText,
        ))
    }

    override fun updatedMark(markId: Long, enText: String, fileName: String): Mono<Void> {
        return markRepository.findById(markId)
            .flatMap { existingMark ->
                val updatedMark = existingMark.updateMark(enText, fileName)
                markRepository.save(updatedMark).then()  // 저장 완료 후 Mono<Void> 반환
            }
    }


    override fun translateStream(shadowingReq: ShadowingReq): Flux<String> {

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

    override fun uploadToS3(fileName: String, dataBuffers: List<DataBuffer>): Mono<String> {
        // 1. DataBuffer 목록을 하나의 ByteArray로 합침
        val byteArrayOutputStream = dataBuffers.fold(ByteArrayOutputStream()) { outputStream, dataBuffer ->
            val byteArray = ByteArray(dataBuffer.readableByteCount())
            dataBuffer.read(byteArray)
            outputStream.write(byteArray)
            outputStream
        }

        // 2. ByteArray를 InputStream으로 변환
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())

        // 3. S3에 업로드할 ObjectMetadata 설정
        val metadata = ObjectMetadata().apply {
            contentLength = byteArrayOutputStream.size().toLong()
            contentType = "audio/mpeg"  // MP3 파일 형식 지정
        }

        return Mono.fromCallable {
            // 4. S3에 파일 업로드
            amazonS3.putObject(bucketName + bucketPath, fileName, byteArrayInputStream, metadata)
            amazonS3.getUrl(bucketName + bucketPath, fileName).toString()  // 업로드된 파일의 URL 반환
        }
    }

    override fun streamS3File(fileName: String): Mono<ResponseEntity<ByteArrayResource>> {
        // S3 객체 가져오기
        val s3Object = amazonS3.getObject(GetObjectRequest(bucketName + bucketPath, fileName))
        val s3InputStream: InputStream = s3Object.objectContent

        // InputStream에서 전체 데이터를 ByteArray로 읽기
        val byteArrayOutputStream = ByteArrayOutputStream()
        s3InputStream.use { input ->
            val buffer = ByteArray(8192)  // 8KB 버퍼 크기
            var bytesRead: Int
            while (input.read(buffer).also { bytesRead = it } != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead)
            }
        }
        val byteArray = byteArrayOutputStream.toByteArray()

        // ByteArray를 ByteArrayResource로 변환하여 ResponseEntity로 반환
        val resource = ByteArrayResource(byteArray)
        return Mono.just(ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$fileName\"")
            .contentType(MediaType.parseMediaType("audio/mpeg"))
            .contentLength(byteArray.size.toLong())
            .body(resource))
    }

}