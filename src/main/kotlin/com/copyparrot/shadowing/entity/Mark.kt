package com.copyparrot.shadowing.entity

import com.copyparrot.util.TimeUtil
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("mark")
class Mark (
    val uuid: String,
    var file: String? = null,
    @Column("ko_text")
    val koText: String,
    @Column("en_text")
    var enText: String? = null,
    @Column("is_save")
    var isSave: Boolean = false,
    @Column("influencer_id")
    var influencerId: Long?  = null,
    @Column("created_date")
    var createdDate: String
) {
    @Id
    var id: Long? = null

    fun updateMark(enText: String, fileName: String, voiceId: Long) : Mark {
        this.enText = enText
        this.file = fileName
        this.createdDate = TimeUtil.getCurrentTimeAsString()
        this.influencerId = voiceId
        return this
    }

    fun voiceSave() : Mark{
        this.isSave = true
        this.createdDate = TimeUtil.getCurrentTimeAsString()
        return this
    }
}