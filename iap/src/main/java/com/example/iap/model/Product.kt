package com.example.iap.model

data class Product(
    val id: String,
    val type: ProductType,
    val isConsumable: Boolean = false
)

enum class ProductType {
    INAPP, SUBS
}