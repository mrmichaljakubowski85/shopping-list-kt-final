package com.tomtre.shoppinglist.android.di

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module

@Module
abstract class AppModule {
    @AppScope
    @Binds
    abstract fun bindContext(app: Application): Context
}