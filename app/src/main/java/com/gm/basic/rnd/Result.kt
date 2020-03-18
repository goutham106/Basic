package com.gm.basic.rnd

/**
 * Author     : Gowtham
 * Email      : goutham.gm11@gmail.com
 * Github     : https://github.com/goutham106
 * Created on : 28/02/20.
 */

sealed class Result<out T : Any> {
    data class Success<out T : Any>(val data: Any) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object InProgress : Result<Nothing>()
}

fun handleResult(result: Result<Int>) {
    when (result) {
        is Result.Success -> {
        }
        is Result.Error -> {
        }
        Result.InProgress -> TODO()
    }.exhaustive
}

val <T> T.exhaustive: T
    get() = this