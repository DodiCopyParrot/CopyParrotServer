package com.copyparrot.products.controller

import com.copyparrot.common.response.BaseResponse
import com.copyparrot.products.dto.ProductDto
import com.copyparrot.products.dto.ProductsCreateReq
import com.copyparrot.products.dto.ProductsCreateRes
import com.copyparrot.products.service.ProductsService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/products")
class ProductsController (
    val productsService: ProductsService
) {

    @PostMapping("")
    fun createProduct(@Valid @RequestBody productsCreateReq: Mono<ProductsCreateReq>) : Mono<BaseResponse<ProductsCreateRes>> {
        return productsCreateReq
            .flatMap { product ->
                productsService.createProduct(product.toEntity())
            }
            .map { res -> BaseResponse(data = res) }
    }

    @GetMapping("")
    fun getAllProducts() : Flux<BaseResponse<ProductDto>> {
        return productsService.getAllProducts()
            .map { productDto ->
                BaseResponse(data = productDto)
            }
    }

    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable(name = "id") id: Long) : Mono<BaseResponse<ProductDto>> {
        return productsService.deleteProduct(id)
            .map { productDto ->
                BaseResponse(data = productDto)
            }
    }

}