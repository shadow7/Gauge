package com.evandenerley.gauge.app

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import butterknife.bindView
import com.evandenerley.gauge.R

/**
 * Main content display for an individual [ItemTemperatureReading]
 */
class TemperatureDisplayActivity : BaseActivity() {
    private val toolbar: Toolbar by bindView(R.id.toolbar_actionbar)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temperature_display)
        toolbar.title = this.javaClass.simpleName
        setSupportActionBar(toolbar)
        supportActionBar.setDisplayHomeAsUpEnabled(true)
        supportActionBar.setHomeButtonEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
