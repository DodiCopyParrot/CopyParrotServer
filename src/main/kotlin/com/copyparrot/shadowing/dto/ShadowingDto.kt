package com.copyparrot.shadowing.dto


class ShadowingReq (
    val uuid: String,
    val koText: String
)

class ShadowingRes (
    val markId: Long,
    val enText: String?
)

class GenerateVoice (
    val markId: Long,
    val voiceId: Long,
    val enText: String
)


