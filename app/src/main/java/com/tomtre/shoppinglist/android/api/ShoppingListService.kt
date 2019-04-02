package com.tomtre.shoppinglist.android.api

import androidx.lifecycle.LiveData
import com.tomtre.shoppinglist.android.vo.Product
import retrofit2.http.GET
import retrofit2.http.Path

interface ShoppingListService {

    @GET("products/{productId}")
    fun getProduct(@Path("productId") productId: Long): LiveData<ApiResponse<Product>>

    @GET("products")
    fun getProducts(): LiveData<ApiResponse<ApiResponse<List<Product>>>>
}