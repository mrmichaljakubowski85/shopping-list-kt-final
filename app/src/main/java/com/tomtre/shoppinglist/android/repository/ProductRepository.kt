package com.tomtre.shoppinglist.android.repository

import androidx.lifecycle.LiveData
import com.tomtre.shoppinglist.android.api.ApiResponse
import com.tomtre.shoppinglist.android.api.ShoppingListService
import com.tomtre.shoppinglist.android.db.ProductDao
import com.tomtre.shoppinglist.android.util.AppExecutors
import com.tomtre.shoppinglist.android.util.RateLimiter
import com.tomtre.shoppinglist.android.vo.Product
import com.tomtre.shoppinglist.android.vo.Resource
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val productDao: ProductDao,
    private val shoppingListService: ShoppingListService
) {
    private val productListRateLimiter = RateLimiter<String>(10, TimeUnit.MINUTES)

    fun forceReload() = productListRateLimiter.reset(Companion.OWNER)

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
                return data.isNullOrEmpty() || productListRateLimiter.shouldFetch(Companion.OWNER)
            }

            override fun loadFromDb(): LiveData<List<Product>> = productDao.loadProducts()

            override fun createCall(): LiveData<ApiResponse<List<Product>>> {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onFetchFailed() {
                productListRateLimiter.reset(Companion.OWNER)
            }
        }.asLiveData()
    }

    fun checkProduct(product: Product) = updateProduct(product)

    fun uncheckProduct(product: Product) = updateProduct(product)

    private fun updateProduct(product: Product): LiveData<Resource<Product>> {
        return object : NetworkBoundResource<Product, Product>(appExecutors) {
            override fun saveCallResult(item: Product) {
                productDao.update(product)
            }

            override fun shouldFetch(data: Product?) = data == null

            override fun loadFromDb(): LiveData<Product> = productDao.findByProductId(product.id)

            override fun createCall(): LiveData<ApiResponse<Product>> {
                return shoppingListService.updateProduct(product)
            }

        }.asLiveData()
    }

    companion object {
        private const val OWNER = "anonymous"
    }


}