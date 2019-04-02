package com.tomtre.shoppinglist.android.vo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey val id: Long,
    val title: String,
    val description: String?
)