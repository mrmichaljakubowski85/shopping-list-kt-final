package com.tomtre.shoppinglist.android.ui.products

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tomtre.shoppinglist.android.repository.ProductRepository
import javax.inject.Inject

//https://proandroiddev.com/livedata-with-single-events-2395dea972a8
class ProductsViewModel @Inject constructor(productsRepository: ProductRepository) : ViewModel() {

    private val refresh = MutableLiveData<Boolean>()

//    val results: LiveData<Resource<List<Product>>> = Transformations
//        .switchMap()

    fun refresh() {

    }
}