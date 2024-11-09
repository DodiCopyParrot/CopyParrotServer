package com.copyparrot.health.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class HealthController {

    @GetMapping("/")
    fun health() : Mono<ResponseEntity<Void>> {
        return Mono.just(ResponseEntity.ok().build())
    }
}