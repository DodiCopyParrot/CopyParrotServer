package com.copyparrot.influencer.repository

import com.copyparrot.influencer.entity.Influencer
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface InfluencerRepository : ReactiveCrudRepository<Influencer, Long> {
    fun findByName(name: String) : Mono<Influencer>

    fun findVoiceFilePathById(voiceId: Long): Mono<String>
}