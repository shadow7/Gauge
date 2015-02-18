package com.evandenerley.gauge.api.rx

import android.os.Parcel
import android.os.Parcelable
import com.shareyourproxy.api.rx.RxHelper
import rx.Observable
import rx.subjects.PublishSubject
import rx.subjects.SerializedSubject
import timber.log.Timber

/**
 * A singleton pattern intended to store an instance in the [ProxyApplication] that allows one
 * to easily send messages over this [PublishSubject] Bus.
 */
object RxBusDriver : Parcelable {
    private val rxBus = SerializedSubject(PublishSubject.create<Any>())

    val CREATOR = object : Parcelable.Creator<RxBusDriver> {
        override fun createFromParcel(parcelIn: Parcel) = this@RxBusDriver
        override fun newArray(size: Int): Array<RxBusDriver?> = arrayOfNulls(size)
    }

    override fun describeContents() = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(rxBus)
    }

    fun toObservable(): Observable<Any> {
        return rxBus.onBackpressureLatest().compose<Any>(RxHelper.observeMain())
    }

    fun toIOThreadObservable(): Observable<Any> {
        return rxBus.onBackpressureLatest().compose<Any>(RxHelper.observeIO())
    }

    /**
     * Post an event on [PublishSubject].

     * @param event event object.
     */
    fun post(event: Any) {
        Timber.i("Event Posted: ${event.toString()}")
        rxBus.onNext(event)
    }
}

