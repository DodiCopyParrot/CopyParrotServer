package com.copyparrot.shadowing.dto

import org.springframework.data.relational.core.mapping.Column


class ShadowingReq (
    val uuid: String,
    val koText: String
)

class ShadowingRes (
    val markId: Long,
    val enText: String?,
    val end: Boolean
)

class GenerateVoice (
    val markId: Long,
    val voiceId: Long,
    val enText: String
)

class RecordVoice (
    val markId: Long
)

class MarkDto (
    val id: Long,
    val uuid: String,
    val file: String? = null,
    val koText: String,
    val enText: String?,
    val name: String?,
    val image: String?,
    val createdDate: String,
    @Column("voice_id")
    val voiceId: Long
)

class ListenVoice (
    val file: String
)

