package com.copyparrot.products.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("products_history")
class ProductsHistory (
    @Column("product_id")
    val productId: Long,
    val count : Long,
    @CreatedDate
    @Column("created_date")
    val createdDate: LocalDateTime,

) {
    @Id
    var id: Long? = null
}



