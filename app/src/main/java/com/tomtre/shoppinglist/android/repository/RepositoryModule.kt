package com.tomtre.shoppinglist.android.repository

import android.app.Application

import androidx.room.Room
import com.tomtre.shoppinglist.android.api.ShoppingListService
import com.tomtre.shoppinglist.android.db.ProductDao
import com.tomtre.shoppinglist.android.db.ShoppingListDb
import com.tomtre.shoppinglist.android.di.AppScope
import com.tomtre.shoppinglist.android.util.LiveDataCallAdapterFactory
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RepositoryModule {

    @AppScope
    @Provides
    fun provideShoppingListService(): ShoppingListService {
        return Retrofit.Builder()
                //todo add const
            .baseUrl("https://")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
            .create(ShoppingListService::class.java)
    }

    @AppScope
    @Provides
    fun provideDb(app: Application): ShoppingListDb {
        return Room.databaseBuilder(app, ShoppingListDb::class.java, "shopping_list.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @AppScope
    @Provides
    fun provideProductDao(db: ShoppingListDb): ProductDao {
        return db.productDao()
    }
}