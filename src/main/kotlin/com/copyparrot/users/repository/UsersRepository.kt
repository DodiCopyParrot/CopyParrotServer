package com.copyparrot.users.repository

import com.copyparrot.users.entity.Users
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface UsersRepository : ReactiveCrudRepository<Users, Long> {
    fun findByUuid(uuid: String): Mono<Users>
}