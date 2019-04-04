package com.tomtre.shoppinglist.android.di

import android.app.Application
import com.tomtre.shoppinglist.android.ShoppingListApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector

@AppScope
@Component(
    modules = [AppModule::class,
        ViewModelModule::class,
        AndroidInjectionModule::class]
)
interface AppComponent : AndroidInjector<ShoppingListApp> {

    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun application(app: Application): Builder
    }
}