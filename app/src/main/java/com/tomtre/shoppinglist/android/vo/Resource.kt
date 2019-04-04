package com.tomtre.shoppinglist.android.vo

import com.tomtre.shoppinglist.android.vo.Status.*

data class Resource<out T>(
    private val status: Status,
    private val data: T?,
    private val message: String?
) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(LOADING, data, null)
        }
    }
}