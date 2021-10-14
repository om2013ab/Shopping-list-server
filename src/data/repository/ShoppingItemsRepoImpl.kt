package com.omarahmed.data.repository

import com.omarahmed.data.models.ShoppingItem
import com.omarahmed.data.requests.UpdateItemRequest
import org.litote.kmongo.SetTo
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.set

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

    override suspend fun updateItem(
        itemId: String,
        updateItemRequest: UpdateItemRequest,
        newImageUrl: String
    ): Boolean {
        return items.updateOne(
            ShoppingItem::id eq itemId,
            set(
                SetTo(ShoppingItem::name, updateItemRequest.newName),
                SetTo(ShoppingItem::imageUrl, newImageUrl),
                SetTo(ShoppingItem::isAddedToCart, updateItemRequest.isAddedToCart)
            )
        ).wasAcknowledged()
    }
}