package com.omarahmed.di

import com.google.gson.Gson
import com.omarahmed.data.repository.shopoingItem.ShoppingItemsRepo
import com.omarahmed.data.repository.shopoingItem.ShoppingItemsRepoImpl
import com.omarahmed.data.repository.user.UserRepository
import com.omarahmed.data.repository.user.UserRepositoryImpl
import com.omarahmed.services.ShoppingItemService
import com.omarahmed.services.UserService
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

    single<UserRepository> {
        UserRepositoryImpl(get())
    }

    single {
        UserService(get())
    }

    single {
        Gson()
    }
}