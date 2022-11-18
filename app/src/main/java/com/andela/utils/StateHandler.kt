package com.andela.utils


sealed class StateHandler<out T> {
    object  Loading : StateHandler<Nothing>()
    class Failure(val msg: Throwable) : StateHandler<Nothing>()
    class Success<out T>(val data: T): StateHandler<T>()
    object Empty : StateHandler<Nothing>()
}

