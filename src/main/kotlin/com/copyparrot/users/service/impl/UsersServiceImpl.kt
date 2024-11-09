package com.copyparrot.users.service.impl

import com.copyparrot.influencer.dto.HomeDto
import com.copyparrot.influencer.dto.InfluencerDto
import com.copyparrot.influencer.dto.UpdateInfluencerReq
import com.copyparrot.influencer.repository.InfluencerRepository
import com.copyparrot.users.dto.OnboardingUserReq
import com.copyparrot.users.dto.OnboardingUserRes
import com.copyparrot.users.repository.UsersRepository
import com.copyparrot.users.service.UsersService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UsersServiceImpl (
    private val usersRepository: UsersRepository,
    private val influencerRepository: InfluencerRepository
) : UsersService {

    /*
    * @param onboardingUserReq
    *
    *
     */
    override fun onboarding(onboardingUserReq: OnboardingUserReq): Mono<OnboardingUserRes> {
        return usersRepository.findByUuid(onboardingUserReq.uuid)
            .doOnNext { println("Existing user found: ${it.id}") }
            .flatMap { existingUser ->
                Mono.just(OnboardingUserRes(
                    id = existingUser.uuid,
                    createdDate = existingUser.createdDate
                ))
            }
            .switchIfEmpty(
                usersRepository.save(onboardingUserReq.toEntity())
                    .doOnNext { println("Saving new user: ${it.id}") }
                    .map { savedUser ->
                        OnboardingUserRes(
                            id = savedUser.uuid,
                            createdDate = savedUser.createdDate
                        )
                    }
            )
    }

    override fun home(uuid: String) : Mono<HomeDto> {
        return usersRepository.findByUuid(uuid)
            .flatMap { existingUser ->
                influencerRepository.findById(existingUser.influencer!!)
                    .map { existingInfluencer ->
                        InfluencerDto(
                            voiceId = existingInfluencer.id!!,
                            context = existingInfluencer.context,
                            image = existingInfluencer.image,
                            name = existingInfluencer.name,
                            voiceFile = existingInfluencer.voiceFile,
                            json = existingInfluencer.json
                        )
                    }
                    .flatMap { existingInfluencer ->
                        Mono.just(HomeDto(
                            message = "오늘 당신이\n말하고 싶 문장은?",
                            detailedMessage = "당신의 워너비, {name} 가 대신 말해줄거에요.".replace("{name}", existingInfluencer.name),
                            influencer = existingInfluencer
                        ))
                    }
            }
    }

    override fun updateInfluencer(updateInfluencerReq: UpdateInfluencerReq): Mono<HomeDto> {
        return usersRepository.findByUuid(updateInfluencerReq.uuid)
            .flatMap { existingUser ->
                influencerRepository.findById(updateInfluencerReq.influencerId)
                    .flatMap { updateInfluencer ->
                        usersRepository.save(existingUser.updateInfluencer(updateInfluencerReq.influencerId))
                            .flatMap { updatedUser ->
                                Mono.just(HomeDto(
                                    message = "오늘 당신이\n말하고 싶 문장은?",
                                    detailedMessage = "당신의 워너비, {name} 가 대신 말해줄거에요.".replace("{name}", updateInfluencer.name),
                                    influencer = InfluencerDto(
                                        voiceId = updateInfluencer.id!!,
                                        context = updateInfluencer.context,
                                        image = updateInfluencer.image,
                                        name = updateInfluencer.name,
                                        voiceFile = updateInfluencer.voiceFile,
                                        json = updateInfluencer.json)))

                            }

                    }
            }
    }


}