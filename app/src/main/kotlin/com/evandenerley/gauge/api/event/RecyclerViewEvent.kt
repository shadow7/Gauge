package com.evandenerley.gauge.api.event

import android.support.v7.widget.RecyclerView
import com.evandenerley.gauge.app.adapter.BaseRecyclerView
import com.evandenerley.gauge.app.adapter.BaseViewHolder


/**
 * Created by Evan on 11/2/15.
 */
class RecyclerViewEvent {
    data class RecyclerViewDatasetChangedEvent(val adapter: RecyclerView.Adapter<BaseViewHolder>, val viewState: BaseRecyclerView.ViewState)
}