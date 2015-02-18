package com.evandenerley.gauge.app

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat.getDrawable
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.View
import android.view.View.OnClickListener
import butterknife.bindView
import com.evandenerley.gauge.R
import com.evandenerley.gauge.api.event.TemperatureEvents.ItemTemperatureReadingAddedEvent
import com.evandenerley.gauge.api.event.TemperatureEvents.ItemTemperatureReadingDismissedEvent
import com.evandenerley.gauge.api.model.ItemTemperatureReading
import com.evandenerley.gauge.api.rx.JustObserver
import com.evandenerley.gauge.api.rx.RxBusDriver
import com.evandenerley.gauge.api.rx.RxBusDriver.post
import com.evandenerley.gauge.app.adapter.ItemTemperatureReadingAdapter
import rx.subscriptions.CompositeSubscription
import timber.log.Timber


class MainActivity : BaseActivity() {
    val DEFAULT_READING = ItemTemperatureReading("New Item", 87f, "Sub Description", ItemTemperatureReading.DEFAULT_RANGE)
    private val toolbar: Toolbar by bindView(R.id.toolbar_actionbar)
    private val drawer: DrawerLayout by bindView(R.id.activity_main_drawer_layout)
    private val addItemButton: FloatingActionButton by bindView(R.id.fragment_main_add_item)
    private val recyclerView: RecyclerView by bindView(R.id.activity_main_recyclerview)
    private val subscriptions = CompositeSubscription()
    private val adapter: ItemTemperatureReadingAdapter = ItemTemperatureReadingAdapter()
    private fun onClickAddItem(): OnClickListener {
        return View.OnClickListener { post(ItemTemperatureReadingAddedEvent(DEFAULT_READING)) }
    }
    private val drawableAdd = getDrawable(this, R.drawable.ic_add)

    public override fun onResume() {
        super.onResume()
        subscriptions.add(RxBusDriver.toObservable().subscribe(activityObserver))
    }

    public override fun onPause() {
        super.onPause()
        subscriptions.unsubscribe()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addItemButton.setImageDrawable(drawableAdd)
        addItemButton.setOnClickListener(onClickAddItem())
        toolbar.title = this.javaClass.simpleName
        setSupportActionBar(toolbar)
        initDrawer()
        initializeRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_drawer, menu)
        return true
    }

    /**
     * Initialize this activities drawer view.
     */
    private fun initDrawer() {
        val mActionBarDrawerToggle = object : ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.common_open, R.string.common_closed) {

            override fun onDrawerOpened(drawerView: View?) {
                super.onDrawerOpened(drawerView)
            }

            override fun onDrawerClosed(drawerView: View?) {
                super.onDrawerClosed(drawerView)
            }
        }
        drawer.setDrawerListener(mActionBarDrawerToggle)
        mActionBarDrawerToggle.syncState()
    }

    private val activityObserver: JustObserver<Any>
        get() = object : JustObserver<Any>() {
            override fun next(event: Any) {
                if (event is ItemTemperatureReadingAddedEvent) {
                    temperatureAddedEvent(event)
                } else if (event is ItemTemperatureReadingDismissedEvent) {
                    temperatureDismissedEvent(event)
                }
            }
        }

    private fun initializeRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.itemAnimator = DefaultItemAnimator()
    }

    fun temperatureAddedEvent(event: ItemTemperatureReadingAddedEvent) {
        Timber.v("Temperature Reading Item Added ${event.toString()}")
        adapter.addItemData(event.itemTemperatureReading)
        adapter.notifyItemInserted(adapter.itemCount)
    }

    fun temperatureDismissedEvent(event: ItemTemperatureReadingDismissedEvent) {
        Timber.v("Temperature Reading Item Removed ${event.toString()}")
        adapter.removeItemData(event.position)
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(event.position) as ItemTemperatureReadingAdapter.ViewHolder
        adapter.notifyDataSetChanged()
        resetViewHolder(viewHolder)
    }

    private fun resetViewHolder(viewHolder: ItemTemperatureReadingAdapter.ViewHolder) {
        viewHolder.itemTempReadingContainer.animate().translationX(0f).alpha(1f).setDuration(0)
        adapter.notifyDataSetChanged()
    }
}
