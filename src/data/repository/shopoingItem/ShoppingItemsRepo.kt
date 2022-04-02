package com.omarahmed.data.repository.shopoingItem

import com.omarahmed.data.models.ShoppingItem
import com.omarahmed.data.requests.UpdateItemRequest
import com.omarahmed.util.Constants

interface ShoppingItemsRepo {

    suspend fun addNewItem(item: ShoppingItem): Boolean

    suspend fun getItemsByUserId(
        userId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE
    ): List<ShoppingItem>

    suspend fun updateItem(
        itemId: String,
        updateItemRequest: UpdateItemRequest
    ): Boolean

    suspend fun updateAllItems(ids: List<String>): Boolean

    suspend fun deleteItem(itemId: String): Boolean

    suspend fun searchForItem(userId: String,query: String): List<ShoppingItem>
}