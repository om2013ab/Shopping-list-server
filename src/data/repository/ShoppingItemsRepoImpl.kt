package com.omarahmed.data.repository

import com.omarahmed.data.models.ShoppingItem
import org.litote.kmongo.coroutine.CoroutineDatabase

class ShoppingItemsRepoImpl(
    db: CoroutineDatabase
): ShoppingItemsRepo {
    private val items = db.getCollection<ShoppingItem>()

    override suspend fun addNewItem(item: ShoppingItem): Boolean {
        return items.insertOne(item).wasAcknowledged()
    }

    override suspend fun getItems(
        page: Int,
        pageSize: Int
    ): List<ShoppingItem> {
        return items.find()
            .skip(page * pageSize)
            .limit(pageSize)
            .descendingSort(ShoppingItem::name)
            .toList()
    }

}