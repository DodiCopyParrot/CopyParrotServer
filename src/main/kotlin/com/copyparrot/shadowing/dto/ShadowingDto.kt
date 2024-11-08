package com.copyparrot.shadowing.dto


class ShadowingReq (
    val voiceId: Long,
    val koText: String
)

class ShadowingRes (
    val enText: String?
)

class GenerateVoice (
    val voiceId: Long,
    val enText: String
)


