package com.omarahmed.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class ShoppingItem(
    val userId: String,
    val name: String,
    val imageUrl: String?,
    val isAddedToCart: Boolean = false,
    @BsonId
    val id: String = ObjectId().toString()
)
