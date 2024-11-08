package com.copyparrot.products.dto

import com.copyparrot.products.entity.Products
import jakarta.validation.constraints.NotBlank

class ProductsCreateReq (
    @field:NotBlank(message = "Category는 필수 값입니다.")
    val category: String,
    @field:NotBlank(message = "Name은 필수 값입니다.")
    val name: String,
    @field:NotBlank(message = "Price은 필수 값입니다.")
    val price: Long,
    @field:NotBlank(message = "TotalCount은 필수 값입니다.")
    val totalCount: Long
) {
    fun toEntity() : Products = Products(
        category = category,
        name = name,
        price = price,
        totalCount = totalCount
    )
}

class ProductsCreateRes (
    val id: Long,
    val category: String,
    val name: String,
    val price: Long,
    val totalCount: Long
)

class ProductDto (
    val id: Long,
    val category: String,
    val name: String,
    val price: Long,
    val totalCount: Long
)