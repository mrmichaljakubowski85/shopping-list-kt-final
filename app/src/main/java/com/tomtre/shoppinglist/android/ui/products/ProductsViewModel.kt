package com.tomtre.shoppinglist.android.ui.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.tomtre.shoppinglist.android.repository.ProductRepository
import com.tomtre.shoppinglist.android.vo.Product
import com.tomtre.shoppinglist.android.vo.Resource

//https://proandroiddev.com/livedata-with-single-events-2395dea972a8
class ProductsViewModel constructor(productsRepository: ProductRepository) : ViewModel() {

    private val refresh = MutableLiveData<Boolean>()

    val results: LiveData<Resource<List<Product>>> = Transformations
        .switchMap()

    fun refresh() {

    }
}