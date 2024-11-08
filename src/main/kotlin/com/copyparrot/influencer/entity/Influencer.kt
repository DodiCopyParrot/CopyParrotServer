package com.copyparrot.influencer.entity

import com.copyparrot.influencer.dto.CreateInfluencerReq
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("influencer")
class Influencer (
    val name: String,
    var image: String?,
    var context: String,
    @Column("voice_file")
    var voiceFile: String?,
    var json: String
) {
    @Id
    var id: Long? = null


    fun updateInfluencerInfo(createInfluencerReq: CreateInfluencerReq) : Influencer {
        this.image = createInfluencerReq.image
        this.context = createInfluencerReq.context
        this.voiceFile = createInfluencerReq.voiceFile
        this.json = createInfluencerReq.json
        return this
    }
}