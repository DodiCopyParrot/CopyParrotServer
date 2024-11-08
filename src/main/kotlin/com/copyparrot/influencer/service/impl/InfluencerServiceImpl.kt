package com.copyparrot.influencer.service.impl

import com.copyparrot.influencer.dto.CreateInfluencerReq
import com.copyparrot.influencer.dto.CreateInfluencerRes
import com.copyparrot.influencer.dto.InfluencerDto
import com.copyparrot.influencer.repository.InfluencerRepository
import com.copyparrot.influencer.service.InfluencerService
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class InfluencerServiceImpl (
    private val influencerRepository: InfluencerRepository
) : InfluencerService {

    override fun generateInfluencer(createInfluencerReq: CreateInfluencerReq): Mono<CreateInfluencerRes> {
        return influencerRepository.findByName(createInfluencerReq.name)
            .flatMap { existingInfluencer ->
                influencerRepository.save(existingInfluencer.updateInfluencerInfo(createInfluencerReq))
                .map { updatedInfluencer ->
                    CreateInfluencerRes(
                        name = updatedInfluencer.name,
                        image = updatedInfluencer.image,
                        context = updatedInfluencer.context,
                        voiceFile = updatedInfluencer.voiceFile,
                        json = updatedInfluencer.json
                    )
                }
            }
            .switchIfEmpty(
                influencerRepository.save(createInfluencerReq.toEntity())
                    .map { savedInfluencer ->
                        CreateInfluencerRes(
                            name = savedInfluencer.name,
                            image = savedInfluencer.image,
                            context = savedInfluencer.context,
                            voiceFile = savedInfluencer.voiceFile,
                            json = savedInfluencer.json
                        ) }
            )
    }

    override fun getInfluencers(): Mono<List<InfluencerDto>> {
        return influencerRepository.findAll()
            .map { existingInfluencer ->
                InfluencerDto(
                    voiceId = existingInfluencer.id!!,
                    context = existingInfluencer.context,
                    image = existingInfluencer.image,
                    name = existingInfluencer.name,
                    voiceFile = existingInfluencer.voiceFile
                )
            }.collectList()
    }

}