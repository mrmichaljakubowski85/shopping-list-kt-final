package com.tomtre.shoppinglist.android.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.tomtre.shoppinglist.android.api.ShoppingListService
import com.tomtre.shoppinglist.android.db.ShoppingListDb
import com.tomtre.shoppinglist.android.util.LiveDataCallAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
abstract class AppModule {

    @AppScope
    @Binds
    abstract fun bindContext(app: Application): Context

    @Module
    companion object {

        private const val BASE_URL = "https://"

        @JvmStatic
        @AppScope
        @Provides
        fun provideShoppingListService(): ShoppingListService {
            return Retrofit.Builder()
                //todo add const
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .build()
                .create(ShoppingListService::class.java)
        }

        @JvmStatic
        @AppScope
        @Provides
        fun provideDb(app: Application): ShoppingListDb {
            return Room.databaseBuilder(app, ShoppingListDb::class.java, "shopping_list.db")
                .fallbackToDestructiveMigration()
                .build()
        }

        @JvmStatic
        @AppScope
        @Provides
        fun provideProductDao(db: ShoppingListDb) = db.productDao()

    }

}