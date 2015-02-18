package com.evandenerley.gauge.api.event

import com.evandenerley.gauge.api.model.ItemTemperatureReading

class TemperatureEvents {
    /**
     * Adds a [ItemTemperatureReading].
     */
    data class ItemTemperatureReadingAddedEvent(val itemTemperatureReading: ItemTemperatureReading)

    /**
     * Remove a [ItemTemperatureReading].
     */
    data class ItemTemperatureReadingDismissedEvent(val position: Int)
}