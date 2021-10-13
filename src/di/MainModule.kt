package com.omarahmed.di

import com.google.gson.Gson
import com.omarahmed.data.models.ShoppingItem
import com.omarahmed.data.repository.ShoppingItemsRepo
import com.omarahmed.data.repository.ShoppingItemsRepoImpl
import com.omarahmed.services.ShoppingItemService
import com.omarahmed.util.Constants.DATABASE_NAME
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val mainModule = module {
    single {
        val client = KMongo.createClient().coroutine
        client.getDatabase(DATABASE_NAME)
    }

    single<ShoppingItemsRepo> {
        ShoppingItemsRepoImpl(get())
    }

    single {
        ShoppingItemService(get())
    }

    single {
        Gson()
    }
}