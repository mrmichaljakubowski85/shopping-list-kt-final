package com.tomtre.shoppinglist.android.di

import android.app.Application
import com.tomtre.shoppinglist.android.ShoppingListApp
import com.tomtre.shoppinglist.android.repository.RepositoryModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector

@AppScope
@Component(
    modules = [AppModule::class,
        RepositoryModule::class]
)
interface AppComponent : AndroidInjector<ShoppingListApp> {
    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun application(app: Application): Builder
    }
}