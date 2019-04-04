package com.tomtre.shoppinglist.android.repository

import androidx.lifecycle.LiveData
import com.tomtre.shoppinglist.android.api.ApiResponse
import com.tomtre.shoppinglist.android.api.ShoppingListService
import com.tomtre.shoppinglist.android.db.ProductDao
import com.tomtre.shoppinglist.android.util.AppExecutors
import com.tomtre.shoppinglist.android.util.RateLimiter
import com.tomtre.shoppinglist.android.vo.Product
import com.tomtre.shoppinglist.android.vo.Resource
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val productDao: ProductDao,
    private val shoppingListService: ShoppingListService
) {
    private val productListRateLimiter = RateLimiter<String>

    fun loadProduct(productId: Long): LiveData<Resource<Product>> {
        return object : NetworkBoundResource<Product, Product>(appExecutors) {
            override fun saveCallResult(item: Product) = productDao.insert(item)

            override fun shouldFetch(data: Product?) = data == null

            override fun loadFromDb(): LiveData<Product> = productDao.findByProductId(productId)

            override fun createCall(): LiveData<ApiResponse<Product>> = shoppingListService.getProduct(productId)

        }.asLiveData()
    }

    fun loadProducts(): LiveData<Resource<List<Product>>> {
        return object : NetworkBoundResource<List<Product>, List<Product>>(appExecutors) {
            override fun saveCallResult(item: List<Product>) = productDao.insertProducts(item)

            override fun shouldFetch(data: List<Product>?): Boolean {
                return data.isNullOrEmpty() ||
            }

            override fun loadFromDb(): LiveData<List<Product>> {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun createCall(): LiveData<ApiResponse<List<Product>>> {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }.asLiveData()
    }
}