package com.copyparrot.shadowing.repository

import com.copyparrot.shadowing.dto.MarkDto
import com.copyparrot.shadowing.entity.Mark
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface MarkRepository : ReactiveCrudRepository<Mark, Long> {
    @Query("""
        SELECT mark.id, mark.uuid, mark.file, mark.ko_text, mark.en_text, i.name, i.image, mark.created_date
        FROM mark
        LEFT JOIN influencer i ON mark.influencer_id = i.id
        WHERE mark.uuid = :uuid AND mark.is_save = :isSave
        ORDER BY mark.created_date
    """)
    fun findByUuidAndIsSaveWithInfluencer(uuid: String, isSave: Boolean): Flux<MarkDto>
}