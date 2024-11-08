package com.copyparrot.users.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("users")
class Users (

    val uuid: String,

    @Column("created_date")
    val createdDate: String
) {
    @Id
    var id: Long? = null
}