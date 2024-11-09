package com.copyparrot.influencer.dto

import com.copyparrot.influencer.entity.Influencer


class CreateInfluencerReq (
    val name: String,
    val image: String?,
    val context: String,
    var voiceFile: String? = null,
    val json: String
) {
    fun toEntity() : Influencer {
        return Influencer(
            name = name,
            image = image,
            context = context,
            voiceFile = voiceFile,
            json = json)
    }
}

class CreateInfluencerRes (
    val name: String,
    val image: String?,
    val context: String,
    var voiceFile: String?,
    val json: String
)

class UpdateInfluencerReq (
    val uuid: String,
    val influencerId: Long
)

class InfluencerDto (
    val voiceId: Long,
    val context: String,
    val image: String?,
    val name: String,
    var voiceFile: String?,
    val json: String
)

class HomeDto (
    val message: String,
    val detailedMessage: String,
    val influencer: InfluencerDto,
)

