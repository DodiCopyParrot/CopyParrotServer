package com.copyparrot.products.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank


class ProductsHistoryReq (
    @field:NotBlank(message = "Id는 필수 값입니다.")
    val id: Long,
    @field:NotBlank(message = "userId는 필수 값입니다.")
    val userId: String,
    @field:Min(1)
    val count: Long
)


class ProductsHistoryRes (
    @field:NotBlank(message = "Id는 필수 값입니다.")
    val id: Long,
    @field:Min(1)
    val count: Long
)