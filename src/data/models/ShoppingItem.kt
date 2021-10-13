package com.omarahmed.data.models

data class ShoppingItem(
    val name: String,
    val imageUrl: String?,
    val isAddedToCart: Boolean = false
)
