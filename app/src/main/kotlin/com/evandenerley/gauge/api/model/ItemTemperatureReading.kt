package com.evandenerley.gauge.api.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Create a Temperature Reading and auto parcel that bean.
 */
data class ItemTemperatureReading(val itemName: String, val lastTemperature: Float, val itemDescription: String, val temperatureRange: TemperatureRange) :
        Parcelable {
    override fun describeContents() = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(itemName)
        dest.writeFloat(lastTemperature)
        dest.writeString(itemDescription)
        dest.writeParcelable(temperatureRange, flags)
    }

    companion object {
        val DEFAULT_RANGE = TemperatureRange(32f, 87f)
        val CL = ItemTemperatureReading::class.java.classLoader
        val CREATOR = object : Parcelable.Creator<ItemTemperatureReading> {
            override fun createFromParcel(parcelIn: Parcel) = readParcel(parcelIn)
            override fun newArray(size: Int): Array<ItemTemperatureReading?> = arrayOfNulls(size)
        }

        private fun readParcel(parcelIn: Parcel) = ItemTemperatureReading(
                parcelIn.readString(), parcelIn.readFloat(), parcelIn.readString(), parcelIn.readParcelable(CL))
    }
}
