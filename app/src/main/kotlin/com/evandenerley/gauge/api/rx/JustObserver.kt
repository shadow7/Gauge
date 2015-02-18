package com.evandenerley.gauge.api.rx

import android.util.Log

import rx.Observer
import timber.log.Timber

/**
 * This abstraction simply logs all errors to the command prompt.
 */
abstract class JustObserver<T> : Observer<T> {
    override fun onCompleted() {
        Timber.v("${this.toString()} onComplete")
    }

    override fun onError(e: Throwable) {
        Timber.e(Log.getStackTraceString(e))
        this.error(e)
    }

    override fun onNext(event: T) {
        this.next(event)
    }

    abstract fun next(event: T)

    fun error(e: Throwable) {
    }
}
