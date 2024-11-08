package com.copyparrot.products.service.impl

import com.copyparrot.common.exception.CustomException
import com.copyparrot.common.status.ResultCode
import com.copyparrot.products.dto.ProductDto
import com.copyparrot.products.dto.ProductsCreateRes
import com.copyparrot.products.entity.Products
import com.copyparrot.products.repository.ProductsRepository
import com.copyparrot.products.service.ProductsService
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class ProductsServiceImpl (
    val productsRepository: ProductsRepository
) : ProductsService {

    override fun createProduct(products: Products) : Mono<ProductsCreateRes> {
        return productsRepository.findByCategory(products.category)
            .flatMap { existingProduct ->
                Mono.just(ProductsCreateRes(
                    id = existingProduct.id!!,
                    category = existingProduct.category,
                    name = existingProduct.name,
                    price = existingProduct.price,
                    totalCount = existingProduct.price
                    ))
            }
            .switchIfEmpty(
                productsRepository.save(products)
                    .map { savedProduct -> ProductsCreateRes(
                        id = savedProduct.id!!,
                        category = savedProduct.category,
                        name = savedProduct.name,
                        price = savedProduct.price,
                        totalCount = savedProduct.price) }
            )

    }

    override fun getAllProducts(): Flux<ProductDto> {
        return productsRepository.findAll()
            .map { existingProduct ->
                ProductDto(
                    id = existingProduct.id!!,
                    category = existingProduct.category,
                    name = existingProduct.name,
                    price = existingProduct.price,
                    totalCount = existingProduct.price)
            }
    }

    override fun deleteProduct(id: Long): Mono<ProductDto> {
        return productsRepository.findById(id)
            .flatMap { product ->
                productsRepository.delete(product)
                    .thenReturn(product)
            }
            .map { deletedProduct ->
                ProductDto(
                    id = deletedProduct.id!!,
                    category = deletedProduct.category,
                    name = deletedProduct.name,
                    price = deletedProduct.price,
                    totalCount = deletedProduct.price)
            }
            .switchIfEmpty(
                Mono.error(CustomException(ResultCode.NOT_FOUND))
            )
    }


}