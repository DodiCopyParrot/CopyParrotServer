package com.copyparrot.shadowing.entity

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
    var isSave: Boolean = false
) {
    @Id
    var id: Long? = null

    fun updateMark(enText: String, fileName: String) : Mark {
        this.enText = enText
        this.file = fileName
        return this
    }
}