package com.omarahmed.data.repository

import com.omarahmed.data.models.ShoppingItem

interface ShoppingItemsRepo {

    suspend fun addNewItem(item: ShoppingItem): Boolean

    suspend fun getItems(): List<ShoppingItem>
}