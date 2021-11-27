package com.omarahmed.data.repository

import com.omarahmed.data.models.ShoppingItem
import com.omarahmed.data.requests.UpdateItemRequest
import com.omarahmed.util.Constants

interface ShoppingItemsRepo {

    suspend fun addNewItem(item: ShoppingItem): Boolean

    suspend fun getItems(
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE
    ): List<ShoppingItem>

    suspend fun updateItem(
        itemId: String,
        updateItemRequest: UpdateItemRequest
    ): Boolean

    suspend fun deleteItem(itemId: String): Boolean

    suspend fun searchForItem(query: String): List<ShoppingItem>
}