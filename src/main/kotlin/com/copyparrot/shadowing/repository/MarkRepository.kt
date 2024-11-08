package com.copyparrot.shadowing.repository

import com.copyparrot.shadowing.entity.Mark
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MarkRepository : ReactiveCrudRepository<Mark, Long> {
}