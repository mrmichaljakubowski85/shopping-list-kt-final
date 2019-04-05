package com.tomtre.shoppinglist.android.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.tomtre.shoppinglist.android.vo.Product

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(product: Product)

    @Update
    fun update(product: Product)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProducts(products: List<Product>)

    @Query("SELECT * FROM products WHERE id = :productId")
    fun findByProductId(productId: Long): LiveData<Product>

    @Query("SELECT * FROM products")
    fun loadProducts(): LiveData<List<Product>>
}