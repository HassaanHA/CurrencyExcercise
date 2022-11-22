package com.andela.core.utils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

open class Observe<out T>(protected val content: T) {

    var hasObserved = false
        protected set

    open fun getContentIfNotHandled(): T {
        return if (hasObserved) {
            throw ObserverException()
        } else {
            hasObserved = true
            content
        }
    }
    open fun peekContent(): T = content
}

class ObserverException(message: String? = "Content already handled") : Exception(message)

class MutableEvent<T : Any?>(value: T? = null) :
    MutableLiveData<Observe<T>>(if (value == null) null else Observe(value)) {
    fun postObserve(value: T) {
        postValue(Observe(value))
    }
    fun setObserve(value: T) {
        setValue(Observe(value))
    }
}

class ObserveObserver<T : Any?>(private val onObserveUnhandledContent: (T) -> Unit) :
    Observer<Observe<T>> {
    override fun onChanged(event: Observe<T>?) {
        try {
            if (event != null) {
                onObserveUnhandledContent(event.getContentIfNotHandled())
            }
        } catch (ehe: ObserverException) {
        }
    }
}