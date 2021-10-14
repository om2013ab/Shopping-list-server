package com.omarahmed.data.repository

import com.omarahmed.data.models.ShoppingItem
import com.omarahmed.util.Constants

interface ShoppingItemsRepo {

    suspend fun addNewItem(item: ShoppingItem): Boolean

    suspend fun getItems(
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE
    ): List<ShoppingItem>
}