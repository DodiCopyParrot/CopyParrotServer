package com.copyparrot.users.service.impl

import com.copyparrot.users.dto.OnboardingUserReq
import com.copyparrot.users.dto.OnboardingUserRes
import com.copyparrot.users.repository.UsersRepository
import com.copyparrot.users.service.UsersService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UsersServiceImpl (
    private val usersRepository: UsersRepository
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


}