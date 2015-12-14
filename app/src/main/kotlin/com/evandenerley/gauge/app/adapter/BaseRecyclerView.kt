package com.evandenerley.gauge.app.adapter


import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.v4.content.ContextCompat.getColor
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.evandenerley.gauge.R
import com.evandenerley.gauge.api.event.RecyclerViewEvent
import com.evandenerley.gauge.api.rx.RxBusDriver

/**
 * [BaseRecyclerView] that handles empty views.
 */
class BaseRecyclerView : RecyclerView {

    private var emptyView: View? = null
    private var loadingView: View? = null
    private val rxBus: RxBusDriver? = null
    var viewType: ViewState? = null
    private var _refreshView: View? = null

    /**
     * Constructor.
     * @param context activity context
     */
    constructor(context: Context) : super(context) {
    }

    /**
     * Constructor.
     * @param context activity context
     * @param attrs   app attributes
     */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    }

    /**
     * Constructor.
     * @param context  activity context
     * @param attrs    app attributes
     * @param defStyle defined style
     */
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
    }

    /**
     * Designate a view as the empty view. When the backing adapter has no data this view will be made visible and the recycler view hidden.

     * @param emptyView the view to display when this adapter has no data
     */
    fun setEmptyView(emptyView: View) {
        this.emptyView = emptyView
    }

    fun setRefreshView(refreshView: View) {
        _refreshView = refreshView
    }

    /**
     * Designate a view as the loading view. When the backing adapter is loading data this view will be made visible and the recycler view hidden.

     * @param loadingView the view to display when this adapter is loading data
     */
    fun setLoadingView(loadingView: View) {
        this.loadingView = loadingView
    }

    override fun setAdapter(adapter: RecyclerView.Adapter<ViewHolder>) {
        // make sure to set a loading or empty view before you call set adapter.
        // That way it will show automatically. Loading views take precedence over empty views.
        super.setAdapter(adapter)
        if (loadingView != null) {
            toggleVisibility(loadingView)
        } else if (emptyView != null) {
            toggleVisibility(emptyView)
        } else {
            toggleVisibility(this)
        }
    }

    /**
     * Show or hide the empty view.
     */
    fun updateViewState(event: RecyclerViewEvent.RecyclerViewDatasetChangedEvent) {
        viewType = event.viewState
        when (viewType) {
            BaseRecyclerView.ViewState.MAIN -> toggleVisibility(this)
            BaseRecyclerView.ViewState.EMPTY -> toggleVisibility(emptyView)
            BaseRecyclerView.ViewState.LOADING -> toggleVisibility(loadingView)
        }
    }

    private fun toggleVisibility(view: View?) {
        if (view != null) {
            clearViewTypeVisibility(view)
            view.visibility = getViewVis(view)
        }
    }

    private fun getViewVis(view: View): Int {
        if (view == this && _refreshView != null) {
            _refreshView!!.visibility = View.VISIBLE
        }
        return View.VISIBLE
    }

    /**
     * Set all ViewState's visibility to be gone.

     * @param view view that will be toggled visible
     */
    private fun clearViewTypeVisibility(view: View) {
        if (view !== this) {
            this.visibility = View.GONE
            if (_refreshView != null) {
                _refreshView!!.visibility = View.GONE
            }
        }
        if (emptyView !== view && emptyView != null) {
            emptyView!!.visibility = View.GONE
        }
        if (loadingView !== view && loadingView != null) {
            loadingView!!.visibility = View.GONE
        }
    }

    /**
     * The type of view state this recycler view is in.
     */
    enum class ViewState {
        MAIN, EMPTY, LOADING
    }


    class SubHeadItemDecoration
    constructor(context: Context, private val _title: String) : RecyclerView.ItemDecoration() {
        private val _textWidth: Int
        private val _textHeight: Int
        internal var _paint: Paint

        init {
            val textView = TextView(context)
            _paint = textView.paint
            _paint.color = getColor(context, R.color.common_text_disabled)
            val textBounds = Rect()
            _paint.getTextBounds(_title, 0, _title.length, textBounds)
            _textWidth = Math.abs(textBounds.width())
            _textHeight = Math.abs(textBounds.height())
        }

        override fun onDrawOver(canvas: Canvas, recyclerView: RecyclerView, state: RecyclerView.State?) {
            super.onDrawOver(canvas, recyclerView, state)
            val size = recyclerView.height + recyclerView.paddingTop + recyclerView.paddingBottom

            canvas.drawText(_title, 60f, 60f, _paint)
        }

        override fun getItemOffsets(
                outRect: Rect, view: View, recyclerView: RecyclerView, state: RecyclerView.State?) {
            super.getItemOffsets(outRect, view, recyclerView, state)
            if (recyclerView.getChildLayoutPosition(view) == 0) {
                outRect.set(0, 0, 0, _textHeight)
                val lp = view.layoutParams as RecyclerView.LayoutParams
                lp.topMargin = _textHeight + PADDING
            }
        }

        companion object {
            val PADDING = 8
        }

    }
}
