package com.copyparrot.products.repository

import com.copyparrot.products.entity.ProductsHistory
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductsHistoryRepository : ReactiveCrudRepository<ProductsHistory, Long> {
}