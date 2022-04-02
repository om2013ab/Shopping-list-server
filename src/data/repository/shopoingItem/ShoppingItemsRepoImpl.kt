package com.omarahmed.data.repository.shopoingItem

import com.mongodb.client.model.InsertOneOptions
import com.omarahmed.data.models.ShoppingItem
import com.omarahmed.data.models.User
import com.omarahmed.data.requests.UpdateItemRequest
import org.litote.kmongo.*
import org.litote.kmongo.coroutine.CoroutineDatabase

class ShoppingItemsRepoImpl(
    db: CoroutineDatabase
): ShoppingItemsRepo {
    private val items = db.getCollection<ShoppingItem>()

    override suspend fun addNewItem(item: ShoppingItem): Boolean {
        return items.insertOne(item).wasAcknowledged()
    }

    override suspend fun getItemsByUserId(
        userId: String,
        page: Int,
        pageSize: Int
    ): List<ShoppingItem> {
        return items.find(ShoppingItem::userId eq  userId)
            .skip(page * pageSize)
            .limit(pageSize)
            .ascendingSort(ShoppingItem::name)
            .toList()
    }

    override suspend fun updateItem(
        itemId: String,
        updateItemRequest: UpdateItemRequest
    ): Boolean {
        return items.updateOne(
            ShoppingItem::id eq itemId,
            setValue(ShoppingItem::isAddedToCart,updateItemRequest.isAddedToCart)
        ).wasAcknowledged()
    }

    override suspend fun updateAllItems(ids: List<String>): Boolean {
        return items.updateMany(
            ShoppingItem::id `in`  ids,
            setValue(ShoppingItem::isAddedToCart,false)
        ).wasAcknowledged()
    }

    override suspend fun deleteItem(itemId: String): Boolean {
        return items.deleteOneById(itemId).wasAcknowledged()
    }

    override suspend fun searchForItem(query: String): List<ShoppingItem> {
        return items.find(
            ShoppingItem::name regex Regex("(?i).*$query.*")
        )
            .descendingSort(ShoppingItem::name)
            .toList()
    }
}