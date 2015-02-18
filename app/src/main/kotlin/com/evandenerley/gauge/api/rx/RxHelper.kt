package com.shareyourproxy.api.rx

import rx.Observable
import rx.Single
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Func1
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

/**
 * RxHelper for common rx.Observable method calls.
 */
object RxHelper {
    fun <T> singleObserveMain(): Single.Transformer<T, T> {
        return Single.Transformer<T, T> { single -> single.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()) }
    }

    fun <T> observeMain(): Observable.Transformer<T, T> {
        return Observable.Transformer<T, T> { observable -> observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()) }
    }

    fun <T> observeIO(): Observable.Transformer<T, T> {
        return Observable.Transformer<T, T> { observable -> observable.subscribeOn(Schedulers.io()).observeOn(Schedulers.io()) }
    }

    fun checkCompositeButton(sub: CompositeSubscription?): CompositeSubscription {
        return sub ?: CompositeSubscription()
    }

    fun <T> filterNullObject(): Func1<T, Boolean> {
        return Func1 { obj -> obj != null }
    }
}
