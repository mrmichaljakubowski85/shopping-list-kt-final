package com.tomtre.shoppinglist.android.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tomtre.shoppinglist.android.vo.Product

@Database(
    entities = [Product::class],
    version = 1,
    exportSchema = true
)
abstract class ShoppingListDb : RoomDatabase() {
    abstract fun productDao(): ProductDao
}