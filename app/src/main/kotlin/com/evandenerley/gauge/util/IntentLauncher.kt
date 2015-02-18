package com.evandenerley.gauge.util

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.evandenerley.gauge.Constants.EXTRA_ITEM_TEMPERATURE_READING
import com.evandenerley.gauge.Intents.ACTION_DISPLAY_TEMPERATURE
import com.evandenerley.gauge.Intents.ACTION_MAIN
import com.evandenerley.gauge.api.model.ItemTemperatureReading

/**
 * Intent utility launcher class for easy accessible one line activity launching. This will be
 * helpful down the road when we will have extras being passed via intents.
 */
object IntentLauncher {

    /**
     * Launch the [MainActivity]
     * @param context The context used to start this intent
     */
    fun launchMainActivity(context: Context) {
        val intent = Intent(ACTION_MAIN)
        context.startActivity(intent)
    }

    /**
     * Launch the [TemperatureDisplayActivity]
     * @param context The context used to start this intent
     */
    fun launchDisplayTemperature(context: Context, itemTemperatureReading: ItemTemperatureReading) {
        val intent = Intent(ACTION_DISPLAY_TEMPERATURE)
        val bundle = Bundle()
        bundle.putParcelable(EXTRA_ITEM_TEMPERATURE_READING, itemTemperatureReading)
        intent.putExtras(bundle)
        context.startActivity(intent)
    }
}
