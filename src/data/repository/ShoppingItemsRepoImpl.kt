package com.omarahmed.data.repository

import com.omarahmed.data.models.ShoppingItem
import org.litote.kmongo.coroutine.CoroutineDatabase

class ShoppingItemsRepoImpl(
    db: CoroutineDatabase
): ShoppingItemsRepo {
    private val items = db.getCollection<ShoppingItem>()

    override suspend fun addNewItem(item: ShoppingItem): Boolean {
        TODO("Not yet implemented")

    }

    override suspend fun getItems(): List<ShoppingItem> {
        TODO("Not yet implemented")
    }

}