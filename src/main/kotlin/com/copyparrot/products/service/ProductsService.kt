package com.copyparrot.products.service

import com.copyparrot.products.dto.ProductDto
import com.copyparrot.products.dto.ProductsCreateRes
import com.copyparrot.products.entity.Products
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ProductsService {
    fun createProduct(products: Products) : Mono<ProductsCreateRes>

    fun getAllProducts() : Flux<ProductDto>

    fun deleteProduct(id: Long) : Mono<ProductDto>
}