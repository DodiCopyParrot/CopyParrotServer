package com.copyparrot.products.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("products")
class Products (

    val category: String,

    val name: String,
    val price: Long,
    @Column("total_count")
    val totalCount: Long
) {
    @Id
    var id: Long? = null
}



