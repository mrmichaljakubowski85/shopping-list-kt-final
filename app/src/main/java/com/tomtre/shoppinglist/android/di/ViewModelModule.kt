package com.tomtre.shoppinglist.android.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tomtre.shoppinglist.android.ui.products.ProductsViewModel
import com.tomtre.shoppinglist.android.viewmodel.ShoppingListViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ProductsViewModel::class)
    abstract fun bindProductsViewModel(productsViewModel: ProductsViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ShoppingListViewModelFactory): ViewModelProvider.Factory
}