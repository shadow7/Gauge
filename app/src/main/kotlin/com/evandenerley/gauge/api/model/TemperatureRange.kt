package com.evandenerley.gauge.api.model

import android.os.Parcel
import android.os.Parcelable

/**
 * TemperatureRange that contains a minimum and maximum acceptable range for an [ ] expect cooking temperature value.
 */
data class TemperatureRange(val minimumTemperature: Float, val maximumTemperature: Float) : Parcelable {
    override fun describeContents() = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeFloat(minimumTemperature)
        dest.writeFloat(maximumTemperature)
    }

    companion object {
        val CREATOR = object : Parcelable.Creator<TemperatureRange> {
            override fun createFromParcel(parcelIn: Parcel) = readParcel(parcelIn)
            override fun newArray(size: Int): Array<TemperatureRange?> = arrayOfNulls(size)
        }

        private fun readParcel(parcelIn: Parcel) = TemperatureRange(parcelIn.readFloat(), parcelIn.readFloat())
    }

}
